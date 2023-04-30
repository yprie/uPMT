package utils.modelControllers.ListView;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import models.ConcreteCategory;
import models.Moment;
import utils.GlobalVariables;
import utils.modelControllers.IModelController;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListView<Model, Controller extends IModelController<Model, Node, ListViewUpdate>> {

    private Pane parentController;
    private Function<Model, Controller> controllerFactory;
    private Function<Controller, Node> nodeFactory;
    private ObservableList<Model> models;
    GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();
    private LinkedList<Controller> indexControllerMap;
    private static Integer noChildFound = -1;
    private Consumer<ListChangeListener.Change<? extends Model>> onListUpdate;
    private ListChangeListener<Model> childChangeListener = change -> {
        while (change.next()) {
            for (Model rem : change.getRemoved()) {
                remove(rem);
            }
            for (Model added : change.getAddedSubList()) {
                add(change.getTo() - 1, added);
            }
            if (onListUpdate != null)
                onListUpdate.accept(change);
        }
    };


    public ListView(ObservableList<Model> models, Function<Model, Controller> controllerFactory, Function<Controller, Node> nodeFactory, Pane parentController) {
        this.parentController = parentController;
        this.indexControllerMap = new LinkedList<>();
        this.controllerFactory = controllerFactory;
        this.nodeFactory = nodeFactory;


        this.models = models;
        for (Model m : this.models)
            add(m);
        this.models.addListener(childChangeListener);
    }

    public void setOnListUpdate(Consumer<ListChangeListener.Change<? extends Model>> onListUpdate) {
        this.onListUpdate = onListUpdate;
    }

    public void add(int index, Model m) {
        if (containsChild(m) == noChildFound) {
            Controller newController = controllerFactory.apply(m);
            Node newNode = nodeFactory.apply(newController);
            getChildren().add(index, newNode);
            indexControllerMap.add(index, newController);
            if (m instanceof Moment) {
                Moment moment = (Moment) m;
                globalVariables.nodeViews.addMoment(moment, moment.getController().nameBox);
            }
            notifyChildren();
            newController.onMount();
            //Platform.runLater(newController::onMount);
        } else {
            throw new IllegalArgumentException("This model element already exists in HBox !");
        }
    }

    public void add(Model m) {
        if (containsChild(m) == noChildFound) {
            Controller newController = controllerFactory.apply(m);
            Node newNode = nodeFactory.apply(newController);

            getChildren().add(newNode);
            int lastChildIndex = getChildren().size() - 1;
            indexControllerMap.add(lastChildIndex, newController);
            if (m instanceof Moment) {
                Moment moment = (Moment) m;
                globalVariables.nodeViews.addMoment(moment, moment.getController().nameBox);

            }
            notifyChildren();
            newController.onMount();
            //Platform.runLater(newController::onMount);
        } else {
            throw new IllegalArgumentException("This model element already exists in HBox !");
        }
    }

    public void remove(Model m) {
        int childIndex = containsChild(m);
        if (childIndex != noChildFound) {
            Controller c = getControllerFromIndex(childIndex);
            c.onUnmount();
            indexControllerMap.remove(childIndex);
            getChildren().remove(childIndex);
            if (m instanceof Moment) {
                Moment moment = (Moment) m;
                globalVariables.nodeViews.removeMoment(moment);

            }
            notifyChildren();
        } else {
            throw new IllegalArgumentException("This model element is not contained in the HBox !");
        }
    }


    private void notifyChildren() {
        int childrenCount = getChildren().size();
        for (int i = 0; i < childrenCount; i++) {
            getControllerFromIndex(i).onUpdate(new ListViewUpdate(i, childrenCount));
        }
    }

    private int containsChild(Model m) {
        for (int i = 0; i < getChildren().size(); i++) {
            if (getModelFromIndex(i) == m)
                return i;
        }
        return -1;
    }

    private Model getModelFromIndex(int i) {
        return getControllerFromIndex(i).getModel();
    }

    public Controller getControllerFromIndex(int i) {
        return indexControllerMap.get(i);
    }

    public void onUnmount() {
        this.models.removeListener(childChangeListener);
        for (int i = 0; i < getChildren().size(); i++) {
            getControllerFromIndex(i).onUnmount();
        }
    }

    private ObservableList<Node> getChildren() {
        return parentController.getChildren();
    }

}

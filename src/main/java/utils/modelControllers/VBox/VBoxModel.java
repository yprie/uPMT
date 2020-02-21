package utils.modelControllers.VBox;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import utils.modelControllers.IModelController;

import java.util.LinkedList;
import java.util.function.Function;

public class VBoxModel<Model, Controller extends IModelController<Model, Node, VBoxModelUpdate>> extends VBox {

    private Function<Model, Controller> controllerFactory;
    private Function<Controller, Node> nodeFactory;

    private LinkedList<Controller> indexControllerMap;
    private static Integer noChildFound = -1;

    public VBoxModel(Function<Model, Controller> controllerFactory, Function<Controller, Node> nodeFactory) {
        this.indexControllerMap = new LinkedList<>();
        this.controllerFactory = controllerFactory;
        this.nodeFactory = nodeFactory;
    }

    public void add(Model m) {
        if(containsChild(m) == noChildFound){
            Controller newController = controllerFactory.apply(m);
            Node newNode = nodeFactory.apply(newController);

            getChildren().add(newNode);
            int lastChildIndex = getChildren().size() - 1;
            indexControllerMap.add(lastChildIndex, newController);
            
            notifyChildren();
            Platform.runLater(() -> {
                newController.onMount();
            });
        }
        else {
            throw new IllegalArgumentException("This model element already exists in VBox !");
        }
    }

    public void remove(Model m) {
        int childIndex = containsChild(m);
        if(childIndex != noChildFound){
            Controller c = getControllerFromIndex(childIndex);
            c.onUnmount();
            indexControllerMap.remove(childIndex);
            getChildren().remove(childIndex);
            notifyChildren();
        }
        else {
            throw new IllegalArgumentException("This model element is not contained in the VBox !");
        }
    }


    private void notifyChildren() {
        int childrenCount =  getChildren().size();
        System.out.println(childrenCount);
        for(int i = 0; i < childrenCount; i++) {
            getControllerFromIndex(i).onUpdate(new VBoxModelUpdate());
        }
    }

    private int containsChild(Model m) {
        for(int i = 0; i < getChildren().size(); i++) {
            if(getModelFromIndex(i) == m)
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
        for(int i = 0; i < getChildren().size(); i++) {
            getControllerFromIndex(i).onUnmount();
        }
    }

}

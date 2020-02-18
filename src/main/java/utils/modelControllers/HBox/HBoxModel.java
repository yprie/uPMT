package utils.modelControllers.HBox;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import utils.modelControllers.IModelController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class HBoxModel<Model, Controller extends IModelController<Model, Node, HBoxModelUpdate>> extends HBox {

    private Function<Model, Controller> controllerFactory;
    private Function<Controller, Node> nodeFactory;

    private LinkedList<Controller> indexControllerMap;
    private static Integer noChildFound = -1;

    public HBoxModel(Function<Model, Controller> controllerFactory, Function<Controller, Node> nodeFactory) {
        this.indexControllerMap = new LinkedList<>();
        this.controllerFactory = controllerFactory;
        this.nodeFactory = nodeFactory;
        this.setAlignment(Pos.CENTER);
    }

    public void add(int index, Model m) {
        if(containsChild(m) == noChildFound){
            Controller newController = controllerFactory.apply(m);
            Node newNode = nodeFactory.apply(newController);

            getChildren().add(index, newNode);
            indexControllerMap.add(index, newController);

            notifyChildren();
        }
        else {
            throw new IllegalArgumentException("This model element already exists in HBox !");
        }
    }

    public void add(Model m) {
        if(containsChild(m) == noChildFound){
            Controller newController = controllerFactory.apply(m);
            Node newNode = nodeFactory.apply(newController);

            getChildren().add(newNode);
            int lastChildIndex = getChildren().size() - 1;
            indexControllerMap.add(lastChildIndex, newController);

            notifyChildren();
        }
        else {
            throw new IllegalArgumentException("This model element already exists in HBox !");
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
            throw new IllegalArgumentException("This model element is not contained in the HBox !");
        }
    }




    private void notifyChildren() {
        int childrenCount =  getChildren().size();
        System.out.println(childrenCount);
        for(int i = 0; i < childrenCount; i++) {
            getControllerFromIndex(i).onUpdate(new HBoxModelUpdate(i, childrenCount));
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

    private Controller getControllerFromIndex(int i) {
        return indexControllerMap.get(i);
    }
}

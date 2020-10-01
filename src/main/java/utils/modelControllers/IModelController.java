package utils.modelControllers;


public interface IModelController<Model, GraphicNode extends javafx.scene.Node, UpdateNotification extends ModelControllerUpdateNotification> {

    Model getModel();

    void onMount();
    void onUpdate(UpdateNotification update);
    void onUnmount();

}

package components.modelisationSpace.appCommand;

import components.modelisationSpace.moment.controllers.MomentController;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import utils.command.Executable;

public class ScrollToMomentCommand implements Executable<Void> {

    private ScrollPane pane;
    private MomentController controller;

    public ScrollToMomentCommand(ScrollPane pane, MomentController controller){
        this.pane = pane;
        this.controller = controller;
    }


    @Override
    public Void execute() {
        //pane.setHvalue();
        if(pane.getContent() != null){
            pane.setHvalue(genHValue());
            pane.setVvalue(genVValue());
        }
        return null;
    }

    private double genHValue() {
        Node p = controller.getBoundingBox();
        Bounds b = pane.getContent().sceneToLocal(p.localToScene(p.getBoundsInLocal()));
        double start = pane.getViewportBounds().getWidth() / 2;
        double end = pane.getContent().getBoundsInLocal().getWidth() - start;
        double a = ((b.getMinX() + b.getMaxX()) / 2);
        return (a - start) / (end - start);
    }

    private double genVValue() {
        Node p = controller.getBoundingBox();
        Bounds b = pane.getContent().sceneToLocal(p.localToScene(p.getBoundsInLocal()));
        double start = pane.getViewportBounds().getHeight() / 2;
        double end = pane.getContent().getBoundsInLocal().getHeight() - start;
        double a = ((b.getMinY() + b.getMaxY()) / 2);
        return (a - start) / (end - start);
    }
}

package components.modelisationSpace.appCommand;

import components.modelisationSpace.moment.controllers.MomentController;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import utils.command.Executable;

public class ScrollToNodeCommand implements Executable<Void> {

    private ScrollPane pane;
    private Node node;

    public ScrollToNodeCommand(ScrollPane pane, Node n){
        this.pane = pane;
        this.node = n;
    }


    @Override
    public Void execute() {
        //pane.setHvalue();
        if(pane.getContent() != null){
            pane.setHvalue(genHValue());
            pane.setVvalue(genVValue());
        }
        else {
            //System.out.println("EMPTY CONTENT !");
        }
        return null;
    }

    private double genHValue() {
        Bounds b = pane.getContent().sceneToLocal(node.localToScene(node.getBoundsInLocal()));
        double start = pane.getViewportBounds().getWidth() / 2;
        double end = pane.getContent().getBoundsInLocal().getWidth() - start;
        double a = ((b.getMinX() + b.getMaxX()) / 2);
        return (a - start) / (end - start);
    }

    private double genVValue() {
        Bounds b = pane.getContent().sceneToLocal(node.localToScene(node.getBoundsInLocal()));
        double start = pane.getViewportBounds().getHeight() / 2;
        double end = pane.getContent().getBoundsInLocal().getHeight() - start;
        double a = ((b.getMinY() + b.getMaxY()) / 2);
        return (a - start) / (end - start);
    }
}

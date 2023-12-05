package utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class ModelisationNavigator {

    ScrollPane scrollPane;
    AnchorPane anchorPane;

    public ModelisationNavigator(ScrollPane scrollPane, AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        this.scrollPane = scrollPane;
    }

    public void centerNodeInScrollPaneX(Node node) {
        double h = this.anchorPane.getBoundsInLocal().getWidth();
        Bounds bounds =
                this.anchorPane.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
        double y = (bounds.getMaxX() +
                bounds.getMinX()) / 2.0;
        double v = this.scrollPane.getViewportBounds().getWidth();
        this.scrollPane.setHvalue(this.scrollPane.getHmax() * ((y - 0.5 * v) / (h - v)));
    }

    public void centerNodeInScrollPaneY(Node node) {
        double h = this.anchorPane.getBoundsInLocal().getHeight();
        Bounds bounds =
                this.anchorPane.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
        double y = (bounds.getMaxY() +
                bounds.getMinY()) / 2.0;
        double v = this.scrollPane.getViewportBounds().getHeight();
        this.scrollPane.setVvalue(this.scrollPane.getHmax() * ((y - 0.5 * v) / (h - v)));
    }
}

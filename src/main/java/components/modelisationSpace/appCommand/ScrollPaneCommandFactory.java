package components.modelisationSpace.appCommand;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class ScrollPaneCommandFactory {

    private ScrollPane pane;

    public ScrollPaneCommandFactory(ScrollPane pane) {
        this.pane = pane;
    }

    public ScrollToNodeCommand scrollToNode(Node node) { return new ScrollToNodeCommand(pane, node); }
}

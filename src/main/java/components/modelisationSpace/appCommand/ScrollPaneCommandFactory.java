package components.modelisationSpace.appCommand;

import components.modelisationSpace.moment.controllers.MomentController;
import javafx.scene.control.ScrollPane;

public class ScrollPaneCommandFactory {

    private ScrollPane pane;

    public ScrollPaneCommandFactory(ScrollPane pane) {
        this.pane = pane;
    }

    public ScrollToMomentCommand scrollToMoment(MomentController momentController) { return new ScrollToMomentCommand(pane, momentController); }
}

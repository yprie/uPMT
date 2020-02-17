package components.modelisationSpace.moment.controllers;

import javafx.scene.layout.Pane;

public class MomentSeparatorController {

    final static int size = 15;
    private Pane p;

    public MomentSeparatorController(boolean vertical) {
        p = new Pane();
        p.setStyle("-fx-background-color: #000");
        if(vertical) {
            p.setMinWidth(size);
        }
        else {
            p.setMinHeight(size);
        }
    }


    public Pane getNode() {
        return p;
    }
}

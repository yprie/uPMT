package components.modelisationSpace.moment.controllers;

import components.interviewPanel.Models.Descripteme;
import components.interviewPanel.Models.InterviewText;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;
import java.util.function.Function;

public class MomentSeparatorController {

    final static int size = 15;
    private Pane p;

    private Consumer<Descripteme> onDragDone = descripteme -> {};

    public MomentSeparatorController(boolean vertical) {
        p = new Pane();
        p.setStyle("-fx-background-color: #000");
        if(vertical) {
            p.setMinWidth(size);
        }
        else {
            p.setMinHeight(size);
        }

        p.setOnMouseClicked(mouseEvent -> {
            onDragDone.accept(new Descripteme(new InterviewText("LOURD"), 0, 1));
        });
    }

    public void setOnDragDone(Consumer<Descripteme> consumer) {
        this.onDragDone = consumer;
    }

    public Pane getNode() {
        return p;
    }
}

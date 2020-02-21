package components.modelisationSpace.moment.controllers;

import components.interviewPanel.Models.Descripteme;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import utils.dragAndDrop.DragStore;

import java.util.function.Consumer;

public class MomentSeparatorController {

    final String style = "-fx-background-radius: 50px;-fx-background-radius: 5, 4;";
    final static int size = 15;
    private Pane p;

    private Consumer<Descripteme> onDragDone = descripteme -> {};

    public MomentSeparatorController(boolean vertical) {
        p = new Pane();
        p.getStyleClass().add("moment-dnd-zone");
        if(vertical) {
            p.setMinWidth(size);
        }
        else {
            p.setMinHeight(size);
        }


/*        p.setOnMouseClicked(mouseEvent -> {
            onDragDone.accept(new Descripteme(new InterviewText("LOURD"), 0, 1));
        });*/

        p.setOnDragEntered(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                p.setStyle(this.style + "-fx-background-color: #80e2ff;");
                dragEvent.consume();
            }
        });

        p.setOnDragOver(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
                dragEvent.consume();
            }
        });

        p.setOnDragDropped(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format){
                onDragDone.accept(DragStore.getDraggable());
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });

        p.setOnDragExited(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                p.setStyle(this.style);
                dragEvent.consume();
            }
        });
    }

    public void setOnDragDone(Consumer<Descripteme> consumer) {
        this.onDragDone = consumer;
    }

    public Pane getNode() {
        return p;
    }
}

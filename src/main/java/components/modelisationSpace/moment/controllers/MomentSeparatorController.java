package components.modelisationSpace.moment.controllers;

import components.interviewPanel.Models.Descripteme;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import utils.dragAndDrop.DragStore;

import java.util.function.Consumer;

public class MomentSeparatorController {

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
                p.setStyle("-fx-background-color: #999;");
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
                onDragDone.accept((Descripteme)DragStore.getDraggable());
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });

        p.setOnDragExited(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                p.setStyle("");
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

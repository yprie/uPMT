package components.modelisationSpace.moment.controllers;

import components.interviewPanel.Models.Descripteme;
import components.interviewPanel.Models.InterviewText;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import utils.dragAndDrop.DragStore;

import java.util.function.Consumer;
import java.util.function.Function;

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

        p.setOnDragOver(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        p.setOnDragDropped(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format){
                onDragDone.accept((Descripteme)DragStore.getDraggable());
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });
    }

    public void setOnDragDone(Consumer<Descripteme> consumer) {
        this.onDragDone = consumer;
    }

    public Pane getNode() {
        return p;
    }
}

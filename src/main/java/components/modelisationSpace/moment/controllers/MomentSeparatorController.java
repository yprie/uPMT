package components.modelisationSpace.moment.controllers;

import components.toolbox.models.SchemaMomentType;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import models.*;
import utils.dragAndDrop.DragStore;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MomentSeparatorController {

    final String style = "-fx-background-radius: 50px;-fx-background-radius: 5, 4;";
    final static int size = 15;
    private Pane p;
    private boolean active = true;

    private Consumer<Descripteme> onDragDoneDescripteme = descripteme -> {};
    private Consumer<ConcreteCategory> onDragDoneCategory = category -> {};
    private Consumer<SchemaCategory> onDragDoneShemaCategory = category -> {};
    private BiConsumer<Moment, RootMoment> onDragMomentDone = (moment, originParentMoment) -> { };
    private Consumer<TemplateMoment> onDragTemplateMoment = templateMoment -> {};
    private Consumer<SchemaMomentType> onDragSchemaMomentType = schemaMomentType -> {};

    public MomentSeparatorController(boolean vertical) {
        p = new Pane();
        p.getStyleClass().add("moment-dnd-zone");
        if(vertical) {
            p.setMinWidth(size);
        }
        else {
            p.setMinHeight(size);
        }

        p.setOnDragEntered(dragEvent -> {
            if(
                DragStore.getDraggable().isDraggable() && acceptDnDElementFormat() && active
            ) {
                /*p.setStyle(this.style + "-fx-background-color: #80e2ff;");*/
                p.setStyle(this.style + "-fx-background-color: #bdc3c7;");
                dragEvent.consume();
            }
        });

        p.setOnDragOver(dragEvent -> {
            if(
                DragStore.getDraggable().isDraggable() && acceptDnDElementFormat() && active
            ) {
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                dragEvent.consume();
            }
        });

        p.setOnDragDropped(dragEvent -> {
            if(dragEvent.isAccepted()) {
                if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format && active){
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragDoneDescripteme.accept(DragStore.getDraggable()); });
                }
                else if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == ConcreteCategory.format && active){
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragDoneCategory.accept(DragStore.getDraggable()); });
                }
                else if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == SchemaCategory.format && active){
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragDoneShemaCategory.accept(DragStore.getDraggable()); });
                }
                else if( DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Moment.format && active){
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragMomentDone.accept(DragStore.getDraggable(), DragStore.getDoubleObject()); });
                }
                else if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == TemplateMoment.format && active) {
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragTemplateMoment.accept(DragStore.getDraggable()); });
                }
                else if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == SchemaMomentType.format && active){
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                    Platform.runLater(() -> { onDragSchemaMomentType.accept(DragStore.getDraggable()); });
                }
            }
        });

        p.setOnDragExited(dragEvent -> {
            if(
                DragStore.getDraggable().isDraggable() && acceptDnDElementFormat()
            ) {
                p.setStyle(this.style);
                dragEvent.consume();
            }
        });
    }

    public void setActive(boolean b) { active = b; }
    public void setOnDragDoneDescripteme(Consumer<Descripteme> consumer) {
        this.onDragDoneDescripteme = consumer;
    }
    public void setOnDragDoneCategory(Consumer<ConcreteCategory> consumer) {
        this.onDragDoneCategory = consumer;
    }
    public void setOnDragDoneShemaCategory(Consumer<SchemaCategory> consumer) {
        this.onDragDoneShemaCategory = consumer;
    }
    public void setOnDragMomentDone(BiConsumer<Moment, RootMoment>consumer){
        this.onDragMomentDone = consumer;
    }

    public void setOnDragTemplateMomentDone(Consumer<TemplateMoment> consumer) {
        this.onDragTemplateMoment = consumer;
    }
    public void setOnDragSchemaMomentType(Consumer<SchemaMomentType> consumer) {
        this.onDragSchemaMomentType = consumer;
    }

    public Pane getNode() {
        return p;
    }

    private boolean acceptDnDElementFormat() {
        return (DragStore.getDraggable().getDataFormat() == Descripteme.format
                || DragStore.getDraggable().getDataFormat() == ConcreteCategory.format
                || DragStore.getDraggable().getDataFormat() == SchemaCategory.format
                || DragStore.getDraggable().getDataFormat() == Moment.format
                || DragStore.getDraggable().getDataFormat() == TemplateMoment.format
                || DragStore.getDraggable().getDataFormat() == SchemaMomentType.format
        );
    }
}

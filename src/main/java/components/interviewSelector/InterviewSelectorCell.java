package components.interviewSelector;

import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import components.modelisationSpace.category.appCommands.RemoveConcreteCategoryCommand;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import models.ConcreteCategory;
import models.Descripteme;
import models.Interview;
import utils.dragAndDrop.DragStore;

public class InterviewSelectorCell extends ListCell<Interview> {
    ObjectProperty<Interview> selectedItem = new SimpleObjectProperty<>();
    private InterviewSelectorCellController controller;
    private InterviewSelectorCommandFactory commandFactory;
    private static int selectedInterviewIndex;

    public InterviewSelectorCell(InterviewSelectorCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (controller != null) controller.setOnHover(true);
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            if (controller != null) controller.setOnHover(false);
        });


    }

    @Override
    public void updateItem(Interview item, boolean empty) {
        super.updateItem(item, empty);

        if (empty)
            removeGraphics();
        else {
            //Cell view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/InterviewSelector/InterviewSelectorCell.fxml"));
            //Cell Controller
            InterviewSelectorCellController newController = new InterviewSelectorCellController(item, this ,commandFactory);
            loader.setController(newController);
            controller = newController;

            //Mouse click event
            setOnMouseClicked(event -> {
                commandFactory.selectCurrentInterview(item, true).execute();
                selectedInterviewIndex=this.getListView().getItems().indexOf(item);
            });
            this.getListView().getStyleClass().add("list-cell");


            newController.updateColor();
            this.addDragAndDrop(newController);

            try {
                this.setGraphic(loader.load());
            } catch (Exception ex) {
                System.out.println("Error on ModelTreeCell graphics update !");
                ex.printStackTrace();
            }
        }
    }
    private void addDragAndDrop(InterviewSelectorCellController newController){
        this.setOnDragDetected(mouseEvent -> {
            selectedItem.set(this.getListView().getSelectionModel().getSelectedItem());
            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(ConcreteCategory.format, 0);
            DragStore.setDraggable(this.getItem());
            db.setContent(content);
            mouseEvent.consume();
        });

        this.setOnDragOver(event -> {
            this.setStyle("-fx-opacity: 1;");
            if (event.getGestureSource() != this &&
                    DragStore.getDraggable().getDataFormat() == Interview.format) {
                this.setStyle("-fx-opacity: 0.5;");
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
            newController.updateColor();
        });
        this.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (DragStore.getDraggable().getDataFormat() == Interview.format) {
                ObservableList<Interview> items = this.getListView().getItems();
                int draggedIdx = items.indexOf(DragStore.getDraggable());
                int thisIdx = items.indexOf(getItem());
                items.set(draggedIdx, getItem());
                items.set(thisIdx, DragStore.getDraggable());
                this.getListView().setItems(items);
                if(selectedInterviewIndex==draggedIdx){
                    this.getListView().getSelectionModel().select(thisIdx);
                    selectedInterviewIndex=thisIdx;
                }else{
                    this.getListView().getSelectionModel().select(selectedInterviewIndex);
                }
                success = true;
                newController.updateColor();
            }

            event.setDropCompleted(success);
            event.consume();
        });


        this.setOnDragExited(dragEvent -> {
            this.setStyle("-fx-opacity: 1;");
            newController.updateColor();
        });

        this.setOnDragEntered(dragEvent -> {
            this.setStyle("-fx-opacity: 1;");
//                newController.updateColor();
        });
    }

    private void removeGraphics() {
        this.setGraphic(null);
    }
}

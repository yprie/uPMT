package components.interviewSelector;

import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import components.modelisationSpace.category.appCommands.RemoveConcreteCategoryCommand;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import models.ConcreteCategory;
import models.Descripteme;
import models.Interview;
import utils.dragAndDrop.DragStore;

public class InterviewSelectorCell extends ListCell<Interview> {

    private InterviewSelectorCellController controller;
    private InterviewSelectorCommandFactory commandFactory;
    public InterviewSelectorCell(InterviewSelectorCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (controller != null) controller.setOnHover(true);
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            if (controller != null) controller.setOnHover(false);
        });
//        this.setOnDragDetected(mouseEvent -> {
//            Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
//            ClipboardContent content = new ClipboardContent();
//            content.put(this.getItem());
//            DragStore.setDraggable(content.get());
//            db.setContent(content);
//            mouseEvent.consume();
//        });
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
            InterviewSelectorCellController newController = new InterviewSelectorCellController(item, commandFactory);
            loader.setController(newController);
            controller = newController;
            //Mouse click event
            setOnMouseClicked(event -> commandFactory.selectCurrentInterview(item, true).execute());


            this.setOnDragDetected(mouseEvent -> {
                Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(ConcreteCategory.format, 0);
                DragStore.setDraggable(this.getItem());
                db.setContent(content);
                mouseEvent.consume();
                System.out.println(this.getItem().getTitle());
            });

            this.setOnDragOver(event -> {
                if (event.getGestureSource() != this &&
                        DragStore.getDraggable().getDataFormat() == Interview.format) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });
            this.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (DragStore.getDraggable().getDataFormat() == Interview.format) {
                    System.out.println(this.getListView().getSelectionModel().getSelectedIndices().get(0));
                    ObservableList<Interview> items = this.getListView().getItems();
                    int draggedIdx = items.indexOf(DragStore.getDraggable());
                    int thisIdx = items.indexOf(getItem());
                    System.out.println(draggedIdx);
                    System.out.println(thisIdx);
                    items.set(draggedIdx, getItem());
                    items.set(thisIdx, DragStore.getDraggable());
                    this.getListView().setItems(items);

                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });


            try {
                this.setGraphic(loader.load());
            } catch (Exception ex) {
                System.out.println("Error on ModelTreeCell graphics update !");
                ex.printStackTrace();
            }
        }
    }

    private void removeGraphics() {
        this.setGraphic(null);
    }
}

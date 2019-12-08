package SchemaTree.Cell;

import ApplicationHistory.HistoryManager;
import ApplicationHistory.HistoryManagerFactory;
import SchemaTree.Cell.Commands.MoveSchemaTreePluggable;
import SchemaTree.Cell.Models.*;
import SchemaTree.Cell.Controllers.SchemaTreeCellController;
import javafx.scene.control.TreeItem;
import utils.ReactiveTree.LeafToRootIterator;
import SchemaTree.Cell.Visitors.CreateControllerVisitor;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeCell;
import javafx.scene.input.*;

import utils.UX.DragStore;


public class SchemaTreeCell extends TreeCell<SchemaTreePluggable> {

    SchemaTreeCellController controller;

    public SchemaTreeCell() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> { if(controller != null)controller.setOnHover(true); });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {if(controller != null)controller.setOnHover(false); });
    }

    public void updateItem(SchemaTreePluggable element, boolean empty) {
        super.updateItem(element, empty);

        if(empty) {
            removeGraphics();

        }
        else {
            createGraphics(element);
            setupDragAndDrop(element);
        }
    }

    private void createGraphics(SchemaTreePluggable element) {
        //Cell view
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SchemaTreeCell.fxml"));

        //Cell Controller
        SchemaTreeCellController newController = updateController(element);
        loader.setController(newController);

        try {
            this.setGraphic(loader.load());
        } catch (Exception ex) {
            System.out.println("Error on ModelTreeCell graphics update !");
            ex.printStackTrace();
        }

        if(controller != null)
            newController.setOnHover(controller.getOnHover());
        controller = newController;
    }


    private void removeGraphics() {
        this.setGraphic(null);
    }

    private SchemaTreeCellController updateController(SchemaTreePluggable element) {
        CreateControllerVisitor visitor = new CreateControllerVisitor();
        element.accept(visitor);
        return visitor.getResultController();
    }

    private void setupDragAndDrop(SchemaTreePluggable element) {

        SchemaTreeCell selfCell = this;
        selfCell.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(element.isDraggable()){
                    Dragboard db = selfCell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.put(element.getDataFormat(), 0);
                    DragStore.setDraggable(element);
                    db.setContent(content);
                }
                mouseEvent.consume();
            }
        });

        selfCell.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != selfCell) {
                    selfCell.setStyle("-fx-font-weight: bold;");
                }
                dragEvent.consume();
            }
        });

        selfCell.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                selfCell.setStyle("-fx-font-weight: normal;");
                event.consume();
            }
        });

        selfCell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if(SchemaTreeCell.checkInternalDrop(event.getDragboard())) {
                    SchemaTreePluggable source = DragStore.<SchemaTreePluggable>getDraggable();
                    SchemaTreePluggable target = selfCell.getItem();

                    //basic checking for drag and drop operation in tree structure.
                    if(source == target || !target.canContain(source)) { event.consume(); return; }

                    //Checking for target being a direct parent of source
                    for(TreeItem i: selfCell.getTreeItem().getChildren())
                        if(i.getValue() == source) { event.consume(); return; }

                    //Checking for source not being an ancestor of the target
                    TreeItem<SchemaTreePluggable> receiverItem  = selfCell.getTreeItem();
                    LeafToRootIterator iterator = new LeafToRootIterator(receiverItem);
                    while (iterator.hasNext())
                        if(iterator.next().getValue() == source) { event.consume(); return; }

                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        selfCell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {

                //Checking if we are in the case of an internal drag and drop (between TreeElementModels)
                if(SchemaTreeCell.checkInternalDrop(event.getDragboard())) {

                    SchemaTreePluggable source = DragStore.<SchemaTreePluggable>getDraggable();
                    DragStore.clearStore();
                    SchemaTreePluggable target = selfCell.getItem();

                    //Drag and drop command
                    HistoryManager m = HistoryManagerFactory.createHistoryManager();
                    m.startNewUserAction();
                    m.addCommand(new MoveSchemaTreePluggable(
                            ((SchemaTreeCell)(event.getGestureSource())).getTreeItem().getParent().getValue(),
                            target,
                            source
                    ));
                    selfCell.getTreeView().getSelectionModel().select(selfCell.getTreeItem());

                    event.setDropCompleted(true);
                }
                else
                    event.setDropCompleted(false);
                event.consume();
            }
        });

        selfCell.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragEvent.consume();
            }
        });

    }

    private void setupOnHover() {

    }

    private static boolean checkInternalDrop(Dragboard db) {
        return (
                db.hasContent(SchemaCategory.format) ||
                        db.hasContent(SchemaFolder.format) ||
                        db.hasContent(SchemaProperty.format)
        );
    }

}

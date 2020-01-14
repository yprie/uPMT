package components.schemaTree.Cell;

import application.history.HistoryManager;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.schemaTree.Cell.modelCommands.MoveSchemaTreePluggable;
import components.schemaTree.Cell.Models.*;
import components.schemaTree.Cell.Controllers.SchemaTreeCellController;
import components.schemaTree.Cell.modelCommands.PermuteSchemaTreePluggable;
import components.schemaTree.Section;
import javafx.scene.control.TreeItem;
import utils.reactiveTree.LeafToRootIterator;
import components.schemaTree.Cell.Visitors.CreateControllerVisitor;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeCell;
import javafx.scene.input.*;

import utils.dragAndDrop.DragStore;


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
        loader.setLocation(getClass().getResource("/views/SchemaTree/SchemaTreeCell.fxml"));

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
        SchemaTreeCommandFactory cmdFactory = new SchemaTreeCommandFactory(getTreeView(), getTreeItem());
        CreateControllerVisitor visitor = new CreateControllerVisitor(cmdFactory);
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
                // TODO: get size of the target
                Section sect = controller.mouseIsDraggingOn(event.getY());
                SchemaTreePluggable source = DragStore.<SchemaTreePluggable>getDraggable();
                SchemaTreePluggable target = selfCell.getItem();

                SchemaTreePluggable Sourceparent = ((SchemaTreeCell)(event.getGestureSource())).getTreeItem().getParent().getValue();
                SchemaTreePluggable Targetparent = selfCell.getTreeItem().getParent().getValue();

                if (sect == Section.middle || Sourceparent != Targetparent) {
                    // we want the same behaviour if the source if moved in another folder
                    if(SchemaTreeCell.checkInternalDrop(event.getDragboard())) {
                        //basic checking for drag and drop operation in tree structure.
                        if(source == target || !target.canContain(source)) { event.consume(); return; }

                        if (isDirectParent(source, selfCell) || isAncestor(source, selfCell)) {
                            event.consume(); return;
                        }

                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                else {
                    if(source == target) {
                        event.consume(); return;
                    }

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

                    Section sect = controller.mouseIsDraggingOn(event.getY());

                    SchemaTreePluggable Sourceparent = ((SchemaTreeCell)(event.getGestureSource())).getTreeItem().getParent().getValue();
                    SchemaTreePluggable Targetparent = selfCell.getTreeItem().getParent().getValue();

                    if (sect == Section.middle || Sourceparent != Targetparent) {
                        //Drag and drop command
                        HistoryManager.addCommand(new MoveSchemaTreePluggable(Sourceparent, target, source), true);
                        selfCell.getTreeView().getSelectionModel().select(selfCell.getTreeItem());
                    }
                    else {
                        int oldIndex = Sourceparent.getChildIndex(source);
                        int newIndex = Sourceparent.getChildIndex(target);

                        if (sect == Section.top && oldIndex < newIndex) {
                            newIndex--;
                        }
                        else if (sect == Section.bottom && oldIndex > newIndex) {
                            newIndex++;
                        }
                        HistoryManager.addCommand(new PermuteSchemaTreePluggable(Sourceparent, oldIndex, newIndex, source), true);
                        // selfCell.getTreeView().getSelectionModel().select(selfCell.getTreeItem()); // TODO: select the target item
                    }
                }
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

    private static boolean isDirectParent(SchemaTreePluggable source, SchemaTreeCell target) {
        //Checking for target being a direct parent of source
        boolean res = false;
        for(TreeItem i: target.getTreeItem().getChildren())
            if(i.getValue() == source) {
                res = true;
            }
        return res;
    }

    private static boolean isAncestor(SchemaTreePluggable source, SchemaTreeCell target){
        //Checking for source not being an ancestor of the target
        TreeItem<SchemaTreePluggable> receiverItem  = target.getTreeItem();
        LeafToRootIterator iterator = new LeafToRootIterator(receiverItem);
        while (iterator.hasNext()) {
            if(iterator.next().getValue() == source) {
                return true;
            }
        }
        return false;
    }

}

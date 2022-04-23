package components.schemaTree.Cell;

import application.history.HistoryManager;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.schemaTree.Cell.modelCommands.MoveSchemaTreePluggable;
import components.schemaTree.Cell.Controllers.SchemaTreeCellController;
import components.schemaTree.Section;
import components.toolbox.controllers.ToolBoxControllers;
import javafx.scene.control.TreeItem;
import models.Moment;
import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
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

        selfCell.setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                selfCell.setStyle("");
                controller.setStyle("");
                event.consume();
            }
        });

        selfCell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (DragStore.getDraggable().getDataFormat() == Moment.format) {
                    Moment m = DragStore.getDraggable();

                    if (ToolBoxControllers.canBeDragged(m)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    } else {
                        event.acceptTransferModes(TransferMode.NONE);
                    }
                } else {
                    boolean accept = false;
                    // TODO: get size of the target
                    Section sect = controller.mouseIsDraggingOn(event.getY());
                    SchemaTreePluggable source = DragStore.getDraggable();
                    SchemaTreePluggable target = selfCell.getItem();

                    SchemaTreePluggable sourceParent = ((SchemaTreeCell)(event.getGestureSource())).getTreeItem()
                            .getParent().getValue();
                    SchemaTreePluggable targetParent = selfCell.getTreeItem().getParent().getValue();

                    if (!isAncestor(source, selfCell) && source != target && !isDirectParent(source, selfCell)) {
                        if (sect == Section.middle) {
                            if (target.canContain(source) && !target.hasChild(source)  && source.canChangeParent()) {
                                selfCell.setStyle("-fx-background-color: #999;-fx-font-weight: bold;");
                                controller.setStyle("");
                                accept = true;
                            }
                        }
                        else {
                            if (canMove(sourceParent, targetParent, source, target, sect)) {
                                if (sect == Section.bottom) {
                                    selfCell.setStyle("-fx-background-color: #999;-fx-font-weight: bold;");
                                    controller.setStyle("-fx-border-color: #777;-fx-border-width: 0 0 4;");
                                }
                                else if (sect == Section.top) {
                                    selfCell.setStyle("-fx-background-color: #999;-fx-font-weight: bold;");
                                    controller.setStyle("-fx-border-color: #777;-fx-border-width: 4 0 0 ;");
                                }
                                accept = true;
                            }
                        }
                    }
                    if (accept) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    else {
                        selfCell.setStyle("");
                        controller.setStyle("");
                    }
                    event.consume();
                }
                event.consume();
            }
        });

        selfCell.setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (DragStore.getDraggable().getDataFormat() == Moment.format) {
                    Moment m = DragStore.getDraggable();
                    ToolBoxControllers.addMomentTypeCommand(m);
                } else {
                    //Checking if we are in the case of an internal drag and drop (between TreeElementModels)
                    if(SchemaTreeCell.checkInternalDrop(event.getDragboard())) {

                        SchemaTreePluggable source = DragStore.<SchemaTreePluggable>getDraggable();
                        DragStore.clearStore();
                        SchemaTreePluggable target = selfCell.getItem();

                        Section sect = controller.mouseIsDraggingOn(event.getY());

                        SchemaTreePluggable sourceParent = ((SchemaTreeCell)(event.getGestureSource())).getTreeItem()
                                .getParent().getValue();
                        SchemaTreePluggable parentTarget = selfCell.getTreeItem().getParent().getValue();

                        SchemaTreePluggable newParent = null;

                        int newIndex = -1;

                        if (sect != Section.middle) {
                            newParent = parentTarget;
                            int oldIndex = sourceParent.getChildIndex(source);
                            newIndex = parentTarget.getChildIndex(target);

                            if (sourceParent == parentTarget) {
                                if (sect == Section.top && oldIndex < newIndex) {
                                    newIndex--;
                                }
                                else if (sect == Section.bottom && oldIndex > newIndex) {
                                    newIndex++;
                                }
                            }
                            else {
                                if (sect == Section.bottom) {
                                    newIndex++;
                                }
                            }
                            selfCell.getTreeView().getSelectionModel().select(selfCell.getTreeView().getSelectionModel()
                                    .getSelectedItem()); // TODO: fix
                        }
                        else {
                            // sect == middle so change parent
                            newParent = target;
                            selfCell.getTreeView().getSelectionModel().select(selfCell.getTreeItem());
                        }
                        HistoryManager.addCommand(new MoveSchemaTreePluggable(sourceParent, newParent, source, newIndex),
                                true);

                    }
                    event.setDropCompleted(false);
                    event.consume();
                }
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

    private static boolean canMove(SchemaTreePluggable sourceParent, SchemaTreePluggable targetParent,
                                   SchemaTreePluggable source, SchemaTreePluggable target, Section section) {
        // Target section is top or bottom: need to check same parent and if the move action will really move something.
        if (target.isSameType(source)) {
            int sourceIndex = sourceParent.getChildIndex(source);
            int targetIndex = targetParent.getChildIndex(target);

            if (sourceParent == targetParent) {
                if (section == Section.top) {
                    if(sourceIndex == targetIndex - 1) {
                        return false;
                    }
                }
                else if (section == Section.bottom) {
                    if(targetIndex == sourceIndex - 1) {
                        return false;
                    }
                }
            }
        }
        else {
            return false;
        }
        return true;
    }
}

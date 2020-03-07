package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import models.Descripteme;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.appCommands.ConcreteCategoryCommandFactory;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import models.ConcreteCategory;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import models.Moment;
import models.SchemaCategory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.util.Duration;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListView;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MomentController extends ListViewController<Moment> implements Initializable {

    private Moment moment;
    private MomentCommandFactory cmdFactory;
    private ConcreteCategoryCommandFactory categoryCmdFactory;
    private MomentCommandFactory childCmdFactory;
    private ScrollPaneCommandFactory paneCmdFactory;

    @FXML private AnchorPane categoryDropper;
    @FXML private BorderPane momentContainer;
    @FXML private BorderPane momentBody;
    @FXML private Label momentName;
    @FXML private Button btn;
    @FXML private MenuButton menuButton;
    @FXML private HBox childrenBox;
    @FXML private VBox categoryContainer;

    //Importants elements of a moment
    private JustificationController justificationController;
    private ListView<Moment, MomentController> momentsHBox;
    private ListView<ConcreteCategory, ConcreteCategoryController> categories;

    @FXML private GridPane grid;
    MomentSeparatorController separatorLeft, separatorRight, separatorBottom;

    public MomentController(Moment m, MomentCommandFactory cmdFactory, ScrollPaneCommandFactory paneCmdFactory) {
        this.moment = m;
        this.cmdFactory = cmdFactory;
        this.categoryCmdFactory = new ConcreteCategoryCommandFactory(moment);
        this.childCmdFactory = new MomentCommandFactory(moment);
        this.paneCmdFactory = paneCmdFactory;
        this.justificationController = new JustificationController(m.getJustification());

        separatorLeft = new MomentSeparatorController(true);
        separatorRight = new MomentSeparatorController(true);
        separatorBottom = new MomentSeparatorController(false);
    }

    public static Node createMoment(MomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/moment/Moment.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid.add(separatorBottom.getNode(), 1, 1);
        momentName.textProperty().bind(moment.nameProperty());

        //Setup de la zone de DND des descriptemes
        momentBody.setCenter(JustificationController.createJustificationArea(justificationController));
        //Setup de la HBox pour les enfants
        momentsHBox = new ListView<>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment,
                childrenBox);
        momentsHBox.setOnListUpdate(change -> {
            if(change.getList().size() == 0)
                separatorBottom.setActive(true);
            else
                separatorBottom.setActive(false);
        });

        categories = new ListView<>(
                moment.concreteCategoriesProperty(),
                (cc -> new ConcreteCategoryController(cc, categoryCmdFactory, paneCmdFactory)),
                ConcreteCategoryController::create,
                categoryContainer
        );


        //Listeners SETUP
        //bottom separator works only when there is no child yet !
        separatorBottom.setOnDragDoneDescripteme(descripteme -> {
                childCmdFactory.addSiblingCommand(new Moment("Moment", descripteme)).execute();
        });
        separatorBottom.setActive(moment.momentsProperty().size() == 0);

        //Menu Button
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.deleteCommand(moment).execute();
        });
        menuButton.getItems().add(deleteButton);

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> {
            cmdFactory.renameCommand(moment).execute();
        });
        menuButton.getItems().add(renameButton);


        //DND
        setupDragAndDrop();
    }

    @Override
    public Moment getModel() {
        return moment;
    }

    @Override
    public void onMount() {
        Timeline viewFocus = new Timeline(new KeyFrame(Duration.seconds(0.1),
            (EventHandler<ActionEvent>) event -> {
                paneCmdFactory.scrollToNode(momentContainer).execute();
        }));
        viewFocus.play();
    }

    @Override
    public void onUpdate(ListViewUpdate update) {
        updateBorders(update.newIndex, update.totalCount);
    }

    @Override
    public void onUnmount() {
        momentsHBox.onUnmount();
        categories.onUnmount();
    }

    private void updateBorders(int index, int siblingsCount) {
        System.out.println("updateBorders" + " " + moment.getName() + " " + index + " " + siblingsCount);
        separatorLeft.setOnDragDoneCategory(category -> { cmdFactory.addSiblingCommand(new Moment("Moment"), category, 0).execute(); });
        separatorRight.setOnDragDoneCategory(category -> { cmdFactory.addSiblingCommand(new Moment("Moment"), category, index+1).execute(); });
        if(index == 0) {
            //Hide an show the separators
            if(grid.getChildren().indexOf(separatorLeft.getNode()) == -1)
                grid.add(separatorLeft.getNode(), 0, 0);
            if(grid.getChildren().indexOf(separatorRight.getNode()) == -1)
                grid.add(separatorRight.getNode(), 2, 0);

            //set operation on descripteme DND over borders
            separatorLeft.setOnDragDoneDescripteme(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), 0).execute(); });
            separatorRight.setOnDragDoneDescripteme(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), index+1).execute(); });


            //Make moment aligned, no need to understand that !
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), ins.getRight()));
        }
        else {
            //Hide an show the separators
            if(grid.getChildren().indexOf(separatorLeft.getNode()) != -1)
                grid.getChildren().remove(separatorLeft.getNode());
            if(grid.getChildren().indexOf(separatorRight.getNode()) == -1)
                grid.add(separatorRight.getNode(), 2, 0);

            //Do nothing with the left separator
            separatorLeft.setOnDragDoneDescripteme(descripteme -> {});
            if(index == siblingsCount - 1) {
                separatorRight.setOnDragDoneDescripteme(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme)).execute(); });
            }
            else {
                separatorRight.setOnDragDoneDescripteme(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), index+1).execute(); });
            }

            //Make moment aligned, no need to understand that !
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), 0));
        }
    }

    private void setupDragAndDrop() {

        categoryDropper.setOnDragOver(dragEvent -> {
            categoryDropper.setStyle("-fx-opacity: 1;");
            if(DragStore.getDraggable().isDraggable()) {
                //Descripteme
                if(
                    !dragEvent.isAccepted()
                    && DragStore.getDraggable().getDataFormat() == Descripteme.format
                    && justificationController.acceptDescripteme(DragStore.getDraggable())
                ){
                    categoryDropper.setStyle("-fx-opacity: 0.5;");
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                }
                //Simple Schema Category
                else if(
                    DragStore.getDraggable().getDataFormat() == SchemaCategory.format
                    && moment.indexOfSchemaCategory(DragStore.getDraggable()) == -1
                ) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    //dragEvent.consume();
                }
                //Existing concrete category
                else if(
                    DragStore.getDraggable().getDataFormat() == ConcreteCategory.format
                    && moment.indexOfConcreteCategory(DragStore.getDraggable()) == -1
                ) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        categoryDropper.setOnDragDropped(dragEvent -> {
            if(
                DragStore.getDraggable().getDataFormat() == Descripteme.format
                && dragEvent.isAccepted()
            ) {
                justificationController.addDescripteme(DragStore.getDraggable());
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
            else if(DragStore.getDraggable().getDataFormat() == SchemaCategory.format){
                categoryCmdFactory.addConcreteCategoryCommand(new ConcreteCategory(DragStore.getDraggable()), true).execute();
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
            else if(DragStore.getDraggable().getDataFormat() == ConcreteCategory.format) {
                categoryCmdFactory.addConcreteCategoryCommand(DragStore.getDraggable(), true).execute();
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
        });

        categoryDropper.setOnDragExited(dragEvent -> {
            System.out.println("exited !");
            categoryDropper.setStyle("-fx-opacity: 1;");
        });

    }

}

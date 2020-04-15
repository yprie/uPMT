package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.appCommands.ConcreteCategoryCommandFactory;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import models.ConcreteCategory;
import models.Descripteme;
import models.Moment;
import models.SchemaCategory;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListView;

import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ModelMomentController extends ListViewController<Moment> implements Initializable {

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
    @FXML private HBox childrenBox;
    @FXML private VBox categoryContainer;
    @FXML private AnchorPane momentBoundingBox;

    @FXML HBox nameBox;

    //Importants elements of a moment
    private JustificationController justificationController;
    private ListView<ConcreteCategory, ConcreteCategoryController> categories;
    private boolean renamingMode = false;

    @FXML private GridPane grid;

    private TextField renamingField;

    public ModelMomentController(Moment m) {
        this.moment = m;
        this.categoryCmdFactory = new ConcreteCategoryCommandFactory(moment);
        this.childCmdFactory = new MomentCommandFactory(moment);
        this.justificationController = new JustificationController(m.getJustification());

    }

    public static Node createMoment(ModelMomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/moment/ModelMoment.fxml"));
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
        momentName.textProperty().bind(moment.nameProperty());

        //Setup de la zone de DND des descriptemes
        momentBody.setCenter(JustificationController.createJustificationArea(justificationController));

        categories = new ListView<>(
                moment.concreteCategoriesProperty(),
                (cc -> new ConcreteCategoryController(cc, categoryCmdFactory, paneCmdFactory)),
                ConcreteCategoryController::create,
                categoryContainer
        );

        momentBody.setOnMouseEntered(event -> momentBody.setStyle("-fx-cursor: move;"));


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
    public void onUpdate(ListViewUpdate update) { }

    @Override
    public void onUnmount() {
        categories.onUnmount();
    }


    private void setupDragAndDrop() {
        momentBody.setOnDragDetected(event -> {
            System.out.println("model moment drag detected");
            Dragboard db = momentBody.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(moment.getDataFormat(), 0);
            Moment newMoment = new Moment(Configuration.langBundle.getString("new_moment"));
            DragStore.setDraggable(newMoment);
            DragStore.setDoubleObject(newMoment);
            db.setContent(content);
            momentBody.setOpacity(0.5);
        });
        momentBody.setOnDragDone(event -> {
            event.consume();
            momentBody.setOpacity(1);
        });

        /*//TODO: garde la possibilité d'ajouter des catégories pour un templete, mais il sert pas comme un templete pour le moment
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
        });*/
    }

}

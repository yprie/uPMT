package components.modelisationSpace.category.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.category.appCommands.RemoveConcreteCategoryCommand;
import javafx.application.Platform;
import javafx.geometry.Insets;
import models.Descripteme;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.appCommands.ConcreteCategoryCommandFactory;
import models.ConcreteCategory;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.property.controllers.ConcretePropertyController;
import models.ConcreteProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListView;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConcreteCategoryController extends ListViewController<ConcreteCategory> implements Initializable {

    private ScrollPaneCommandFactory paneCommandFactory;
    private ConcreteCategoryCommandFactory cmdFactory;
    private ConcreteCategory category;
    private JustificationController justificationController;
    private ListView<ConcreteProperty, ConcretePropertyController> properties;

    @FXML private BorderPane container;
    @FXML private Label name;
    @FXML private MenuButton menuButton;
    @FXML private VBox propertiesContainer;

    //Listeners
    private ChangeListener<Boolean> onSchemaTreeRemoving = (ChangeListener<Boolean>) (observableValue, aBoolean, t1) -> {
        if(!t1) {
            cmdFactory.removeConcreteCategoryCommand(category, false).execute();
        }
    };

    public ConcreteCategoryController(
            ConcreteCategory c,
            ConcreteCategoryCommandFactory cmdFactory,
            ScrollPaneCommandFactory paneCommandFactory
    ) {
        this.category = c;
        this.cmdFactory = cmdFactory;
        this.paneCommandFactory = paneCommandFactory;
        this.justificationController = new JustificationController(c.getJustification());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.getSchemaCategory().addToControllers(this);
        category.setController(this);
        name.textProperty().bind(category.nameProperty());
        VBox justif = (VBox)JustificationController.createJustificationArea(justificationController);
        justif.setPadding(new Insets(0, 0, 0, 10));
        container.setCenter(justif);
        updateColor();

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeConcreteCategoryCommand(category, true).execute();
        });
        menuButton.getItems().add(deleteButton);

        properties = new ListView<>(
                category.propertiesProperty(),
                (property) -> { return new ConcretePropertyController(cmdFactory.getHookNotifier(), property); },
                ConcretePropertyController::create,
                propertiesContainer
        );

        setupDragAndDrop();
    }

    public static Node create(ConcreteCategoryController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/concreteCategory/ConcreteCategory.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupDragAndDrop() {

        container.setOnDragDetected(mouseEvent -> {
            Dragboard db = container.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(ConcreteCategory.format, 0);
            DragStore.setDraggable(category);
            db.setContent(content);
            mouseEvent.consume();
        });

        container.setOnDragDone(dragEvent -> {
            if (dragEvent.getTransferMode() == TransferMode.MOVE) {
                RemoveConcreteCategoryCommand c = cmdFactory.removeConcreteCategoryCommand(DragStore.getDraggable(), false);
                Platform.runLater(c::execute);
            }
        });


        container.setOnDragOver(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
            if(
                !dragEvent.isAccepted()
                && DragStore.getDraggable().getDataFormat() == Descripteme.format
            ){
                if(justificationController.acceptDescripteme(DragStore.getDraggable())) {
                    container.setStyle("-fx-opacity: 0.5;");
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                else {
                    dragEvent.acceptTransferModes(TransferMode.NONE);
                }
            }
        });

        container.setOnDragDropped(dragEvent -> {
            if(DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                if(justificationController.acceptDescripteme(DragStore.getDraggable())) {
                    justificationController.addDescripteme(DragStore.getDraggable());
                    dragEvent.setDropCompleted(true);
                    dragEvent.consume();
                }
                //dragEvent.consume();
            }
        });

        container.setOnDragExited(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
            container.setStyle("-fx-background-color: #" + category.getSchemaCategory().getColor() + ";\n");
        });

        container.setOnDragEntered(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
        });
    }

    public void updateColor() {
        container.setStyle("-fx-background-color: #" + category.getSchemaCategory().getColor() + ";\n");
    }

    @Override
    public ConcreteCategory getModel() {
        return category;
    }

    @Override
    public void onMount() {
        Timeline viewFocus = new Timeline(new KeyFrame(Duration.seconds(0.1),
                (EventHandler<ActionEvent>) event -> { paneCommandFactory.scrollToNode(container).execute(); }));
        viewFocus.play();
    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {

    }


}

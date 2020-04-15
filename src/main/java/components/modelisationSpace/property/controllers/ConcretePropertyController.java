package components.modelisationSpace.property.controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import models.Descripteme;
import components.modelisationSpace.justification.controllers.JustificationController;
import models.ConcreteProperty;
import components.modelisationSpace.property.modelCommands.EditConcretePropertyValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyProperty;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;
import utils.popups.TextEntryController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConcretePropertyController extends ListViewController<ConcreteProperty> implements Initializable {

    private ConcreteProperty property;
    private JustificationController justificationController;

    @FXML private BorderPane container;
    @FXML private Label name;
    @FXML private Label value;
    @FXML private HBox justificationZone;
    @FXML private HBox head;

    public ConcretePropertyController(ConcreteProperty p) {
        property = p;
        justificationController = new JustificationController(property.getJustification());
    }

    public static Node create(ConcretePropertyController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/concreteProperty/ConcreteProperty.fxml"));
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
        name.textProperty().bind(property.nameProperty());
        value.textProperty().bind(property.valueProperty());
        VBox justif = (VBox)JustificationController.createJustificationArea(justificationController);
        justif.setPadding(new Insets(0, 0, 0, 20));
        container.setCenter(justif);

        head.setOnMouseClicked(mouseEvent -> {
            TextEntryController c = TextEntryController.enterText(
                    property.getName(),
                    property.getValue(),
                    20,
                    new SuggestionStrategyProperty()
            );
            //c.setStrategy();
            if(c.getState() == DialogState.SUCCESS){
                HistoryManager.addCommand(new EditConcretePropertyValue(property, c.getValue()), true);
            }
        });

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {

        container.setOnDragEntered(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
        });

        container.setOnDragOver(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
            if(
                !dragEvent.isAccepted()
                && DragStore.getDraggable().getDataFormat() == Descripteme.format
            ){
                if(justificationController.acceptDescripteme(DragStore.getDraggable())){
                    container.setStyle("-fx-opacity: 0.5;");
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                else {
                    //dragEvent.acceptTransferModes(TransferMode.NONE);
                }
            }
        });

        container.setOnDragDropped(dragEvent -> {
            if(
                DragStore.getDraggable().getDataFormat() == Descripteme.format
                && justificationController.acceptDescripteme(DragStore.getDraggable())
            ) {
                justificationController.addDescripteme(DragStore.getDraggable());
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
        });

        container.setOnDragExited(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
        });
    }

    @Override
    public ConcreteProperty getModel() {
        return property;
    }

    @Override
    public void onMount() {
    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {

    }
}

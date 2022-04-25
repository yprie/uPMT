package components.modelisationSpace.property.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.property.appCommands.EditConcretePropertyCommand;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import models.Descripteme;
import components.modelisationSpace.justification.controllers.JustificationController;
import models.ConcreteProperty;
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
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConcretePropertyController extends ListViewController<ConcreteProperty> implements Initializable {

    private ConcreteProperty property;
    private JustificationController justificationController;
    private ModelisationSpaceHookNotifier modelisationSpaceHookNotifier;
    private boolean renamingMode;
    private TextField renamingField;

    @FXML private BorderPane container;
    @FXML private Label name;
    @FXML private Label value;
    @FXML private HBox justificationZone;
    @FXML private HBox head;

    public ConcretePropertyController(ModelisationSpaceHookNotifier modelisationSpaceHookNotifier, ConcreteProperty p) {
        this.modelisationSpaceHookNotifier = modelisationSpaceHookNotifier;
        property = p;
        justificationController = new JustificationController(property.getJustification());
        this.renamingMode = false;
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
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    passInRenamingMode();
                }
            }
        });

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {

        container.setOnDragEntered(dragEvent -> container.setStyle("-fx-opacity: 1;"));

        container.setOnDragOver(dragEvent -> {
            container.setStyle("-fx-opacity: 1;");
            if(
                !dragEvent.isAccepted()
                        && DragStore.getDraggable().getDataFormat() == Descripteme.format
                        && justificationController.acceptDescripteme(DragStore.getDraggable())
            ){
                container.setStyle("-fx-opacity: 0.5;");
                dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
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

        container.setOnDragExited(dragEvent -> container.setStyle("-fx-opacity: 1;"));
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


    public void passInRenamingMode() {
        if (renamingMode) return;
        renamingField = new AutoSuggestionsTextField(value.getText(), new SuggestionStrategyMoment());

        renamingField.setAlignment(Pos.CENTER_LEFT);
        renamingField.end();
        renamingField.selectAll();

        int indexOfValue = this.head.getChildren().indexOf(value);

        renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal){   //unfocus
                exitRenamingMode(indexOfValue, false);
            }
        });

        renamingField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                exitRenamingMode(indexOfValue, false);
            }

            if(keyEvent.getCode() == KeyCode.ESCAPE) {
                exitRenamingMode(indexOfValue, true);
            }
        });
        this.head.getChildren().remove(value);
        this.head.getChildren().add(indexOfValue, renamingField);
        renamingField.requestFocus();
        renamingMode = true;
    }

    private void exitRenamingMode(int indexOfValue, boolean canceled) {
        if (!renamingMode) return;

        if(!canceled && renamingField.getLength() > 0 && !value.getText().equals(renamingField.getText())) {
            new EditConcretePropertyCommand(modelisationSpaceHookNotifier, property, renamingField.getText(), true).execute();
        }

        this.head.getChildren().remove(renamingField);
        this.head.getChildren().add(indexOfValue, value);
        renamingMode = false;
    }


}

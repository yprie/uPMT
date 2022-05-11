package components.toolbox.controllers;

import application.configuration.Configuration;
import components.toolbox.appCommand.RenameMomentTypesCommand;
import models.SchemaMomentType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import models.Moment;
import models.*;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MomentTypeController implements Initializable {
    private @FXML BorderPane momentTypeBorderPane;
    private @FXML Label momentTypeLabel;
    private SchemaMomentType schemaMomentType;

    private boolean renamingMode = false;
    private AutoSuggestionsTextField renamingField;

    public MomentTypeController(Moment moment) {
        this.schemaMomentType = new SchemaMomentType(moment, this);
    }

    public MomentTypeController(SchemaMomentType schemaMomentType) {
        this.schemaMomentType = schemaMomentType;
    }

    public static Node createMomentTypeController(MomentTypeController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/Toolbox/components/MomentType.fxml"));
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
        this.momentTypeLabel.textProperty().bind(this.schemaMomentType.nameProperty());
        this.momentTypeBorderPane.setStyle("-fx-background-color: #"+this.schemaMomentType.getColor()+";");
        setupDragAndDrop();
        updatePopUp();

        this.momentTypeBorderPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    passInRenamingMode(true);
                }
            }
        });

        schemaMomentType.nameProperty().addListener((observableValue, o, t1) -> updatePopUp());
    }

    private void setupDragAndDrop() {
       momentTypeBorderPane.setOnDragDetected(event -> {
           Dragboard db = momentTypeBorderPane.startDragAndDrop(TransferMode.MOVE);
           ClipboardContent content = new ClipboardContent();
           content.put(SchemaMomentType.format, 0);
           DragStore.setDraggable(schemaMomentType);
           db.setContent(content);
           event.consume();
       });
    }

    private void updatePopUp() {
        StringBuilder message = new StringBuilder(schemaMomentType.getName() + "\n");
        for (SchemaCategory sc : schemaMomentType.categoriesProperty()) {
            message.append('\n').append(sc.getName()).append(" :\n");
            for (SchemaProperty sp : sc.propertiesProperty()) {
                message.append("\t- ").append(sp.getName()).append("\n");
            }
        }

        Tooltip.install(momentTypeBorderPane, new Tooltip(message.toString()));
    }

    public boolean exists(Moment moment) {
        return moment.getName().equals(this.schemaMomentType.getName());
    }

    public SchemaMomentType getSchemaMomentType() {
        return schemaMomentType;
    }

    public void passInRenamingMode(boolean YoN) {
        if (YoN != renamingMode) {
            if (YoN) {
                renamingField = new AutoSuggestionsTextField(schemaMomentType.getName(), new SuggestionStrategyMoment());
                renamingField.setAlignment(Pos.BASELINE_CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (renamingField.getLength() > 0) {
                            new RenameMomentTypesCommand(this.schemaMomentType, renamingField.getText()).execute();
                        }
                        passInRenamingMode(false);
                    }
                });

                this.momentTypeBorderPane.setCenter(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            } else {
                this.momentTypeBorderPane.setCenter(momentTypeLabel);
                renamingMode = false;
            }
        }
    }
}

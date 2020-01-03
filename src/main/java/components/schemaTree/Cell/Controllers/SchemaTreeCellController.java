package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.schemaTree.Cell.Commands.RenameSchemaTreePluggable;
import components.schemaTree.Cell.SchemaTreePluggable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class SchemaTreeCellController implements Initializable {

    @FXML
    BorderPane nameDisplayer;

    @FXML
    Label name;

    TextField renamingField;

    @FXML
    ImageView pictureView;

    @FXML
    MenuButton optionsMenu;

    protected SchemaTreePluggable element;
    private boolean renamingMode = false;
    private boolean shouldRemoveMenuButtonVisibility;

    public SchemaTreeCellController(SchemaTreePluggable element) {
        this.element = element;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage(element.getIconPath()));

        name.setText(element.nameProperty().get());
        this.name.textProperty().bind(element.nameProperty());

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> {
            passInRenamingMode(true);
        });
        optionsMenu.getItems().add(renameButton);

        optionsMenu.setVisible(false);
        optionsMenu.onHiddenProperty().addListener((observableValue, eventEventHandler, t1) -> {
            if(shouldRemoveMenuButtonVisibility) { shouldRemoveMenuButtonVisibility = false; optionsMenu.setVisible(false);}
        });

        Platform.runLater(()-> { if(element.mustBeRenamed()) passInRenamingMode(true); });
    }


    public void passInRenamingMode(boolean YoN) {
        if(YoN != renamingMode) {
            if(YoN){
                renamingField = new TextField(name.getText());
                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER) {
                        if(renamingField.getLength() > 0)
                            HistoryManager.addCommand(new RenameSchemaTreePluggable(element, renamingField.getText()), !element.mustBeRenamed());
                        passInRenamingMode(false);
                    }
                });

                this.nameDisplayer.setLeft(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            }
            else {
                this.nameDisplayer.setLeft(name);
                renamingMode = false;
            }
        }
    }

    public void setOnHover(boolean YoN) {
        if(optionsMenu.isShowing())
            shouldRemoveMenuButtonVisibility = true;
        else
            optionsMenu.setVisible(YoN);
    }

    public boolean getOnHover() { return optionsMenu.isVisible(); }
}

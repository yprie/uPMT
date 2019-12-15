package Components.InterviewTree.Controller;

import Components.InterviewTree.Commands.RenameInterviewTreePluggable;
import Components.InterviewTree.InterviewTreePluggable;
import Components.SchemaTree.Cell.Commands.RenameSchemaTreePluggable;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import application.Configuration.Configuration;
import application.History.HistoryManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class InterviewTreeCellController implements Initializable {

    @FXML
    BorderPane nameDisplayer;

    @FXML
    Label name;

    TextField renamingField;

    @FXML
    MenuButton optionsMenu;

    @FXML
    ImageView imageView;

    @FXML
    ImageView imageIcon;

    protected InterviewTreePluggable element;
    private boolean renamingMode = false;
    private boolean shouldRemoveMenuButtonVisibility;

    public InterviewTreeCellController(InterviewTreePluggable element) {
        this.element = element;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(element.nameProperty().get());
        this.name.textProperty().bind(element.nameProperty());
        imageIcon.setImage(ResourceLoader.loadImage(element.getIconPath()));

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> {
            passInRenamingMode(true);
        });
        optionsMenu.getItems().add(renameButton);
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
                        HistoryManager.addCommand(new RenameInterviewTreePluggable(element, renamingField.getText()), !element.mustBeRenamed());
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
        //TODO: change text color here
        if(optionsMenu.isShowing()) {
            shouldRemoveMenuButtonVisibility = true;
        }
        else {
            optionsMenu.setVisible(YoN);
        }
    }

    public boolean getOnHover() {
        return optionsMenu.isVisible();
    }
}

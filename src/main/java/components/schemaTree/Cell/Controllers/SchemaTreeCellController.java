package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.schemaTree.Section;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import utils.ResourceLoader;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategy;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class SchemaTreeCellController implements Initializable {
    @FXML private BorderPane container;

    @FXML
    BorderPane nameDisplayer;

    @FXML
    Label name;

    @FXML Label complementaryInfo;

    AutoSuggestionsTextField renamingField;

    @FXML
    ImageView pictureView;

    @FXML
    MenuButton optionsMenu;

    protected SchemaTreePluggable element;
    private boolean renamingMode = false;
    private boolean shouldRemoveMenuButtonVisibility;
    private SchemaTreeCommandFactory cmdFactory;

    public SchemaTreeCellController(SchemaTreePluggable element, SchemaTreeCommandFactory cmdFactory) {
        this.element = element;
        this.cmdFactory = cmdFactory;
    }

    protected abstract SuggestionStrategy getSuggestionStrategy();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage(element.getIconPath()));

        name.setText(element.nameProperty().get());
        name.textProperty().bind(element.nameProperty());

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> {
            passInRenamingMode(true);
        });
        optionsMenu.getItems().add(renameButton);

        optionsMenu.setVisible(false);
        optionsMenu.onHiddenProperty().addListener((observableValue, eventEventHandler, t1) -> {
            if(shouldRemoveMenuButtonVisibility) { shouldRemoveMenuButtonVisibility = false; optionsMenu.setVisible(false);}
        });

        Platform.runLater(()-> {
            if(element.mustBeRenamed())
                passInRenamingMode(true);
        });
    }


    public void passInRenamingMode(boolean YoN) {
        if(YoN != renamingMode) {
            if(YoN){
                renamingField = new AutoSuggestionsTextField(name.getText());
                renamingField.setStrategy(this.getSuggestionStrategy());
                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER) {
                        if(renamingField.getLength() > 0){
                            cmdFactory.renameTreeElement(element, renamingField.getText());
                        }
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

    public Section mouseIsDraggingOn(double y) {
        if(y < 10) {
            return Section.top;
        }
        else if (y > 20){
            return Section.bottom;
        }
        else {
            return Section.middle;
        }
    }

    public void createPane(Section sect) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:#f4b4b4;");
        pane.setOpacity(0.2);
        pane.setPrefSize(230, 5);
    }

    public void setStyle(String style) {
        container.setStyle(style);
    }
}

package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import models.SchemaFolder;
import models.SchemaTreeRoot;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;
import utils.autoSuggestion.strategies.SuggestionStrategy;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeRootController extends SchemaTreeCellController {

    @FXML
    BorderPane container;

    SchemaTreeRoot root;
    SchemaTreeCommandFactory cmdFactory;

    public SchemaTreeRootController(SchemaTreeRoot root, SchemaTreeCommandFactory cmdFactory) {
        super(root, cmdFactory);
        this.root = root;
        this.cmdFactory = cmdFactory;
    }

    @Override
    protected SuggestionStrategy getSuggestionStrategy() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage(element.getIconPath()));
        name.setText(element.nameProperty().get());

        optionsMenu.setDisable(true);
        optionsMenu.setStyle("-fx-opacity: 0;");
    }
}

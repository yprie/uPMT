package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.schemaTree.Cell.modelCommands.AddSchemaTreePluggable;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeRootController extends SchemaTreeCellController {

    @FXML
    BorderPane container;

    SchemaTreeRoot root;

    public SchemaTreeRootController(SchemaTreeRoot root) {
        super(root);
        this.root = root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage(element.getIconPath()));
        name.setText(element.nameProperty().get());

        MenuItem addFolderButton = new MenuItem(Configuration.langBundle.getString("add_folder"));
        addFolderButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(root, new SchemaFolder(Configuration.langBundle.getString("folder")), true), true);
        });
        optionsMenu.getItems().add(addFolderButton);
        optionsMenu.setVisible(false);
    }
}

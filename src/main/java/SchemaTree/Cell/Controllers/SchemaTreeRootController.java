package SchemaTree.Cell.Controllers;

import ApplicationHistory.HistoryManager;
import ApplicationHistory.HistoryManagerFactory;
import SchemaTree.Cell.Commands.AddSchemaTreePluggable;
import SchemaTree.Cell.Models.SchemaFolder;
import SchemaTree.Cell.Models.SchemaTreeRoot;
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

        MenuItem addFolderButton = new MenuItem("Nouveau dossier");
        addFolderButton.setOnAction(actionEvent -> {
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            AddSchemaTreePluggable cmd = new AddSchemaTreePluggable(root, new SchemaFolder("dossier"), true);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(addFolderButton);
        optionsMenu.setVisible(false);
    }
}

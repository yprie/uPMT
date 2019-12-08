package SchemaTree.Cell.Controllers;

import ApplicationHistory.HistoryManager;
import ApplicationHistory.HistoryManagerFactory;
import NewModel.Commands.DeleteRemovableCommand;
import SchemaTree.Cell.Commands.AddSchemaTreePluggable;
import SchemaTree.Cell.Models.SchemaCategory;
import SchemaTree.Cell.Models.SchemaFolder;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeFolderController extends SchemaTreeCellController {

    private SchemaFolder folder;

    public SchemaTreeFolderController(SchemaFolder folder) {
        super(folder);
        this.folder = folder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        MenuItem addFolderButton = new MenuItem("Nouveau dossier");
        addFolderButton.setOnAction(actionEvent -> {
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            AddSchemaTreePluggable cmd = new AddSchemaTreePluggable(folder, new SchemaFolder("dossier"), true);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(addFolderButton);

        MenuItem addCategoryButton = new MenuItem("Nouvelle catégorie");
        addCategoryButton.setOnAction(actionEvent -> {
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            AddSchemaTreePluggable cmd = new AddSchemaTreePluggable(folder, new SchemaCategory("catégorie"), true);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(addCategoryButton);

        MenuItem deleteButton = new MenuItem("Supprimer");
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            DeleteRemovableCommand cmd = new DeleteRemovableCommand(folder);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

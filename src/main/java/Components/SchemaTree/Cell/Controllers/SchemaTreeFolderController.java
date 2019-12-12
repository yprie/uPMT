package Components.SchemaTree.Cell.Controllers;

import application.History.HistoryManager;
import utils.Removable.Commands.DeleteRemovableCommand;
import Components.SchemaTree.Cell.Commands.AddSchemaTreePluggable;
import Components.SchemaTree.Cell.Models.SchemaCategory;
import Components.SchemaTree.Cell.Models.SchemaFolder;
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
            HistoryManager.addCommand(new AddSchemaTreePluggable(folder, new SchemaFolder("dossier"), true), true);
        });
        optionsMenu.getItems().add(addFolderButton);

        MenuItem addCategoryButton = new MenuItem("Nouvelle catégorie");
        addCategoryButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(folder, new SchemaCategory("catégorie"), true), true);
        });
        optionsMenu.getItems().add(addCategoryButton);

        MenuItem deleteButton = new MenuItem("Supprimer");
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new DeleteRemovableCommand(folder), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

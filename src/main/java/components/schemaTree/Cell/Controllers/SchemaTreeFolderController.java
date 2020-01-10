package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.modelCommands.RemoveSchemaTreePluggable;
import components.schemaTree.Cell.modelCommands.AddSchemaTreePluggable;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeFolderController extends SchemaTreeCellController {

    private SchemaFolder folder;
    private SchemaTreePluggable parent;

    public SchemaTreeFolderController(SchemaTreePluggable parent, SchemaFolder folder) {
        super(folder);
        this.folder = folder;
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        MenuItem addFolderButton = new MenuItem(Configuration.langBundle.getString("add_folder"));
        addFolderButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(folder, new SchemaFolder(Configuration.langBundle.getString("folder")), true), true);
        });
        optionsMenu.getItems().add(addFolderButton);

        MenuItem addCategoryButton = new MenuItem(Configuration.langBundle.getString("add_category"));
        addCategoryButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(folder, new SchemaCategory("category"), true), true);
        });
        optionsMenu.getItems().add(addCategoryButton);

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new RemoveSchemaTreePluggable<SchemaFolder>(parent, folder), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

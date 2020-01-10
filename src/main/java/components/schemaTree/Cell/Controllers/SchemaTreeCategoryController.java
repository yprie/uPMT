package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.modelCommands.RemoveSchemaTreePluggable;
import components.schemaTree.Cell.modelCommands.AddSchemaTreePluggable;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeCategoryController extends SchemaTreeCellController {

    private SchemaCategory category;
    private SchemaTreePluggable parent;

    public SchemaTreeCategoryController(SchemaTreePluggable parent, SchemaCategory category) {
        super(category);
        this.category = category;
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        MenuItem addPropertyButton = new MenuItem(Configuration.langBundle.getString("add_property"));
        addPropertyButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(category, new SchemaProperty(Configuration.langBundle.getString("property")), true), true);
        });
        optionsMenu.getItems().add(addPropertyButton);

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new RemoveSchemaTreePluggable<SchemaCategory>(parent, category), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

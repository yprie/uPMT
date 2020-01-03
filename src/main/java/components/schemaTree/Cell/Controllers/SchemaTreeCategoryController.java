package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import utils.removable.Commands.DeleteRemovableCommand;
import components.schemaTree.Cell.Commands.AddSchemaTreePluggable;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeCategoryController extends SchemaTreeCellController {

    private SchemaCategory category;

    public SchemaTreeCategoryController(SchemaCategory category) {
        super(category);
        this.category = category;
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
            HistoryManager.addCommand(new DeleteRemovableCommand(category), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

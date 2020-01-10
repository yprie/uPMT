package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.modelCommands.RemoveSchemaTreePluggable;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;


public class SchemaTreePropertyController extends SchemaTreeCellController {

    private SchemaProperty property;
    private SchemaTreePluggable parent;

    public SchemaTreePropertyController(SchemaTreePluggable parent, SchemaProperty property) {
        super(property);
        this.property = property;
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new RemoveSchemaTreePluggable<SchemaProperty>(parent, property), true);
        });

        optionsMenu.getItems().add(deleteButton);
    }
}

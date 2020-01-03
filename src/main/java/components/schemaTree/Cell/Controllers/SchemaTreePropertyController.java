package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import utils.removable.Commands.DeleteRemovableCommand;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;


public class SchemaTreePropertyController extends SchemaTreeCellController {

    private SchemaProperty property;

    public SchemaTreePropertyController(SchemaProperty property) {
        super(property);
        this.property = property;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new DeleteRemovableCommand(property), true);
        });

        optionsMenu.getItems().add(deleteButton);
    }
}

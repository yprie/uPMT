package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;


public class SchemaTreePropertyController extends SchemaTreeCellController {

    private SchemaProperty property;
    private SchemaTreeCommandFactory cmdFactory;

    public SchemaTreePropertyController(SchemaProperty property, SchemaTreeCommandFactory cmdFactory) {
        super(property);
        this.property = property;
        this.cmdFactory = cmdFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeTreeElement(property).execute();
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

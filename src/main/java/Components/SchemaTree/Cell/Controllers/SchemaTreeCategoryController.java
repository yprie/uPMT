package Components.SchemaTree.Cell.Controllers;

import application.History.HistoryManager;
import utils.Removable.Commands.DeleteRemovableCommand;
import Components.SchemaTree.Cell.Commands.AddSchemaTreePluggable;
import Components.SchemaTree.Cell.Models.SchemaCategory;
import Components.SchemaTree.Cell.Models.SchemaProperty;
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

        MenuItem addPropertyButton = new MenuItem("Nouvelle propriété");
        addPropertyButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new AddSchemaTreePluggable(category, new SchemaProperty("propriété"), true), true);
        });
        optionsMenu.getItems().add(addPropertyButton);

        MenuItem deleteButton = new MenuItem("Supprimer");
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new DeleteRemovableCommand(category), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

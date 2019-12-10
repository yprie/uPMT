package Components.SchemaTree.Cell.Controllers;

import application.History.HistoryManager;
import application.History.HistoryManagerFactory;
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
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            AddSchemaTreePluggable cmd = new AddSchemaTreePluggable(category, new SchemaProperty("propriété"), true);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(addPropertyButton);

        MenuItem deleteButton = new MenuItem("Supprimer");
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            DeleteRemovableCommand cmd = new DeleteRemovableCommand(category);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

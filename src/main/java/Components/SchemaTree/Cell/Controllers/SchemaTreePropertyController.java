package Components.SchemaTree.Cell.Controllers;

import application.History.HistoryManager;
import utils.Removable.Commands.DeleteRemovableCommand;
import Components.SchemaTree.Cell.Models.SchemaProperty;
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
        MenuItem deleteButton = new MenuItem("Supprimer");
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new DeleteRemovableCommand(property), true);
        });

        optionsMenu.getItems().add(deleteButton);
    }
}

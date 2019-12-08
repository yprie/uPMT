package SchemaTree.Cell.Controllers;

import ApplicationHistory.HistoryManager;
import ApplicationHistory.HistoryManagerFactory;
import NewModel.Commands.DeleteRemovableCommand;
import SchemaTree.Cell.Models.SchemaProperty;
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
            HistoryManager hm = HistoryManagerFactory.createHistoryManager();
            DeleteRemovableCommand cmd = new DeleteRemovableCommand(property);
            hm.startNewUserAction();
            hm.addCommand(cmd);
        });

        optionsMenu.getItems().add(deleteButton);
    }
}

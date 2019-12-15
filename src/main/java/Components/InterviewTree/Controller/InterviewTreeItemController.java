package Components.InterviewTree.Controller;

import Components.InterviewTree.Cell.Model.InterviewElement;
import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Commands.DeleteItemPluggable;
import Components.SchemaTree.Cell.Controllers.SchemaTreeCellController;
import Components.SchemaTree.Cell.Models.SchemaProperty;
import application.Configuration.Configuration;
import application.History.HistoryManager;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import utils.Removable.Commands.DeleteRemovableCommand;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;


public class InterviewTreeItemController extends InterviewTreeCellController {

    private InterviewItem element;

    public InterviewTreeItemController(InterviewItem element) {
        super(element);
        this.element = element;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(new DeleteRemovableCommand(element), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

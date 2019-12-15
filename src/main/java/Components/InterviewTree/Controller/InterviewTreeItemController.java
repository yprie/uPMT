package Components.InterviewTree.Controller;

import Components.InterviewTree.Cell.Model.InterviewItem;

import application.Configuration.Configuration;
import application.History.HistoryManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import utils.Removable.Commands.DeleteRemovableCommand;
import java.net.URL;
import java.util.Optional;
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
            if (confirmDelete())
                HistoryManager.addCommand(new DeleteRemovableCommand(element), true);
        });
        optionsMenu.getItems().add(deleteButton);
    }

    private boolean confirmDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Configuration.langBundle.getString("delete_warning"));
        alert.setHeaderText(Configuration.langBundle.getString("delete_interview"));
        alert.setContentText(Configuration.langBundle.getString("delete_interview_text_alert"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}

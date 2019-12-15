package Components.InterviewTree.Controller;

import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.InterviewTree.Commands.AddInterviewPluggable;
import Components.SchemaTree.Cell.Commands.AddSchemaTreePluggable;
import Components.SchemaTree.Cell.Controllers.SchemaTreeCellController;
import Components.SchemaTree.Cell.Models.SchemaFolder;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;
import application.Configuration.Configuration;
import application.History.HistoryManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class InterviewTreeRootController extends InterviewTreeCellController {

    @FXML
    BorderPane container;

    InterviewTreeRoot root;

    public InterviewTreeRootController(InterviewTreeRoot root) {
        super(root);
        this.root = root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageIcon.setImage(ResourceLoader.loadImage(element.getIconPath()));
        name.setText(element.nameProperty().get());

        MenuItem addFolderButton = new MenuItem(Configuration.langBundle.getString("add_interview"));
        addFolderButton.setOnAction(actionEvent -> {
            HistoryManager.addCommand(
                    new AddInterviewPluggable(root, new InterviewItem(Configuration.langBundle.getString("interview")),
                            false), true);
            ;        });
        optionsMenu.getItems().add(addFolderButton);
        optionsMenu.setVisible(false);
    }
}

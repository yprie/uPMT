package Components.InterviewTree.Controller;

import Components.InterviewPanel.Models.InterviewText;
import Components.InterviewSelector.Models.Interview;
import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.InterviewTree.Commands.AddInterviewPluggable;
import application.Configuration.Configuration;
import application.History.HistoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;
import java.net.URL;
import java.time.LocalDate;
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

        MenuItem addInterviewButton = new MenuItem(Configuration.langBundle.getString("add_interview"));
        addInterviewButton.setOnAction(actionEvent -> {
            //TODO: replace with opening new interview panel
            Interview testInterview = new Interview("John", LocalDate.now(), new InterviewText("testejkjdfksldfjdslkjldfjsdlkfjsl\nflsdjflkdsjflkdsjfl"));
            HistoryManager.addCommand(
                    new AddInterviewPluggable(root, new InterviewItem(testInterview),
                            false), true);
            ;        });
        optionsMenu.getItems().add(addInterviewButton);
        optionsMenu.setVisible(false);
    }


}

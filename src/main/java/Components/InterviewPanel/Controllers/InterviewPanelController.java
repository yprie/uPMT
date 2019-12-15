package Components.InterviewPanel.Controllers;

import Components.InterviewPanel.Models.InterviewText;
import Components.InterviewSelector.Models.Interview;
import application.Configuration.Configuration;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class InterviewPanelController implements Initializable {
    private Interview interview;
    private boolean collapsed = false;
    private double panePosition;

    static private BorderPane root;
    @FXML private TextArea textInterview;
    @FXML private Text textInterviewTitle;
    @FXML private Label textInterviewComment;
    @FXML private ImageView buttonCollapseInterviewPanel;
    @FXML private BorderPane headerGrid;
    @FXML private StackPane stackForDragDrop;
    @FXML private BorderPane topBarContainerTextInterview;
    private SplitPane mainSplitPane;

    public InterviewPanelController(Interview interview, SplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
        // DEBUG >>>
        interview = new Interview("Joseph", LocalDate.now(), new InterviewText("blablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhoho"));
        this.interview = interview;
        this.interview.setComment("La description est ici.La description est ici.La description est ici.La description est ici.La description est ici.La description est ici.La description est ici.");
        // <<< DEBUG
    }

    public static Node createInterviewPanel(InterviewPanelController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewPanel/InterviewPanel.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            root = (BorderPane) loader.load();
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        textInterview.setText(interview.getInterviewText().getText());
        textInterviewTitle.setText(interview.getParticipantName());
        textInterviewComment.setText(interview.getComment());
        panePosition = mainSplitPane.getDividers().get(1).getPosition();
    }

    // Events:
    @FXML
    private void buttonCollapseInterviewPanelOnMouseClicked(MouseEvent mouseEvent) {
        if (!collapsed) {
            // close
            buttonCollapseInterviewPanel.setImage(new Image("/images/openMenuBlack.png"));
            topBarContainerTextInterview.setCenter(null);
            root.setCenter(null);
            mainSplitPane.setDividerPosition(1,1.0);
        } else {
            // open
            buttonCollapseInterviewPanel.setImage(new Image("/images/closeMenuBlack.png"));
            topBarContainerTextInterview.setCenter(headerGrid);
            root.setCenter(stackForDragDrop);
            //root.setMaxWidth(root.USE_COMPUTED_SIZE);
            mainSplitPane.setDividerPosition(1,panePosition);
        }
        collapsed = !collapsed;
    }
}

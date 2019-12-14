package Components.InterviewPanel.Controllers;

import Components.InterviewPanel.Models.InterviewText;
import Components.InterviewSelector.Models.Interview;
import application.Configuration.Configuration;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class InterviewPanelController implements Initializable {
    private Interview interview;

    @FXML
    private TextArea textInterview;
    @FXML private Text textInterviewTitle;
    @FXML private Text textInterviewComment;

    public InterviewPanelController(Interview interview) {
        // DEBUG >>>
        interview = new Interview("the title", "Joseph", LocalDate.now(), new InterviewText("blablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablablahohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhohoblablabla\nhoho"));
        this.interview = interview;
        this.interview.setComment("C'est Joseph de la compta qui a peur du vide");
        // <<< DEBUG
    }

    public static Node createInterviewPanel(InterviewPanelController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewPanel/InterviewPanel.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
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
    }
}

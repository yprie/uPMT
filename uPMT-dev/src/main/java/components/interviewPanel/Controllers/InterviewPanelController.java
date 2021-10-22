package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import models.Interview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewPanelController implements Initializable {
    @FXML private BorderPane container;
    @FXML private Text textInterviewTitle;
    @FXML private Label textInterviewComment;
    @FXML private ImageView buttonCollapseInterviewPanel;
    @FXML private BorderPane headerGrid;
    @FXML private BorderPane topBarContainerTextInterview;

    private double panePosition;
    private final SplitPane mainSplitPane;

    private final ObservableValue<Interview> interview;
    private final ChangeListener<Interview> interviewChangeListener;
    private ChangeListener<String> titleChangeListener;
    private ChangeListener<String> commentChangeListener;
    private Node interviewTextController;

    public InterviewPanelController(ObservableValue<Interview> interview, SplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
        this.interview = interview;

        this.interviewChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                interview.getValue().commentProperty().addListener(commentChangeListener);
                interview.getValue().titleProperty().addListener(titleChangeListener);
                if (oldValue != null) {
                    oldValue.commentProperty().removeListener(commentChangeListener);
                    oldValue.titleProperty().removeListener(titleChangeListener);
                }
                refreshContent(newValue);
            }
        };

        this.commentChangeListener = (observableValue, oldValue, newValue) -> textInterviewComment.setText(newValue);
        this.titleChangeListener = (observableValue, oldValue, newValue) -> textInterviewTitle.setText(newValue);
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
        bind();
        refreshContent(interview.getValue());
        panePosition = mainSplitPane.getDividers().get(1).getPosition();
    }

    private void bind() {
        interview.addListener(interviewChangeListener);
        if (interview.getValue() != null) {
            interview.getValue().commentProperty().addListener(commentChangeListener);
            interview.getValue().titleProperty().addListener(titleChangeListener);
        }
    }

    public void unbind() {
        interview.removeListener(interviewChangeListener);
        interview.getValue().commentProperty().removeListener(commentChangeListener);
        interview.getValue().titleProperty().removeListener(titleChangeListener);
    }

    private void refreshContent(Interview newInterview) {
        if(newInterview != null) {
            textInterviewTitle.setText(newInterview.getTitle());
            textInterviewComment.setText(newInterview.getComment());
            textInterviewComment.setVisible(true);
            interviewTextController = InterviewTextController.createInterviewTextController(interview.getValue());
            container.setCenter(interviewTextController);
        }
        else {
            textInterviewTitle.setText(Configuration.langBundle.getString("no_interview_selected"));
            textInterviewComment.setVisible(false);
            container.setCenter(null);
        }
    }
}

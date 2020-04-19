package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import models.Interview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewPanelController implements Initializable {

    @FXML private Text textInterviewTitle;
    @FXML private Label textInterviewComment;
    @FXML private ImageView buttonCollapseInterviewPanel;
    @FXML private BorderPane headerGrid;
    @FXML private BorderPane topBarContainerTextInterview;
    @FXML private StackPane stackPaneInterview;

    private boolean collapsed = false;
    private double panePosition;
    private SplitPane mainSplitPane;
    private InterviewTextController interviewTextController;


    private ObservableValue<Interview> interview;
    private ChangeListener<Interview> interviewChangeListener;
    private ChangeListener<String> titleChangeListener;
    private ChangeListener<String> commentChangeListener;

    public InterviewPanelController(ObservableValue<Interview> interview, SplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
        this.interview = interview;

        System.out.println("in panel: "+interview.toString());

        this.interviewChangeListener = (observable, oldValue, newValue) -> {
                interview.getValue().commentProperty().addListener(commentChangeListener);
                oldValue.commentProperty().removeListener(commentChangeListener);
                interview.getValue().titleProperty().addListener(titleChangeListener);
                oldValue.titleProperty().removeListener(titleChangeListener);
                refreshContent(newValue);
        };

        this.commentChangeListener = (observableValue, oldValue, newValue) -> { textInterviewComment.setText(newValue); };
        this.titleChangeListener = (observableValue, oldValue, newValue) -> { textInterviewTitle.setText(newValue); };
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

        buttonCollapseInterviewPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                if (!collapsed) {
                    // close
                    buttonCollapseInterviewPanel.setImage(new Image("/images/openMenuBlack.png"));
                    topBarContainerTextInterview.setCenter(null);
                    hideTextInterview();
                    mainSplitPane.setDividerPosition(1,1.0);
                } else {
                    // open
                    buttonCollapseInterviewPanel.setImage(new Image("/images/closeMenuBlack.png"));
                    topBarContainerTextInterview.setCenter(headerGrid);
                    if (interview.getValue() != null) {
                        showTextInterview(interview.getValue());
                    }
                    mainSplitPane.setDividerPosition(1,panePosition);
                }
                collapsed = !collapsed;
            }
        });
    }

    private void bind() {
        interview.addListener(interviewChangeListener);
        interview.getValue().commentProperty().addListener(commentChangeListener);
        interview.getValue().titleProperty().addListener(titleChangeListener);
    }

    public void unbind() {
        interview.removeListener(interviewChangeListener);
        interview.getValue().commentProperty().removeListener(commentChangeListener);
        interview.getValue().titleProperty().removeListener(titleChangeListener);
    }
    
    private void refreshContent(Interview newInterview) {
        if (!collapsed) {
            if(newInterview != null) {
                textInterviewTitle.setText(newInterview.getParticipantName());
                textInterviewComment.setText(newInterview.getComment());
                textInterviewComment.setVisible(true);
                showTextInterview(newInterview);
            }
            else {
                textInterviewTitle.setText(Configuration.langBundle.getString("no_interview_selected"));
                textInterviewComment.setVisible(false);
                hideTextInterview();
            }
        }
    }

    private void showTextInterview(Interview newInterview) {
        stackPaneInterview.getChildren().add(InterviewTextController.createInterviewTextController(newInterview));
    }

    private void hideTextInterview() {
        stackPaneInterview.getChildren().clear();
    }
}

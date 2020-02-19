package components.interviewPanel.Controllers;

import components.interviewSelector.models.Interview;
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
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewPanelController implements Initializable {

    @FXML private Text textInterviewTitle;
    @FXML private Label textInterviewComment;
    @FXML private ImageView buttonCollapseInterviewPanel;
    @FXML private BorderPane headerGrid;
    @FXML private BorderPane topBarContainerTextInterview;
    @FXML private StackPane borderPaneCenter;

    //private boolean collapsed = false;
    private double panePosition;
    private SplitPane mainSplitPane;

    private ObservableValue<Interview> interview;
    private ChangeListener<Interview> interviewChangeListener;

    public InterviewPanelController(ObservableValue<Interview> interview, SplitPane mainSplitPane) {
        this.mainSplitPane = mainSplitPane;
        this.interview = interview;
        this.interviewChangeListener = (observable, oldValue, newValue) -> refreshContent(newValue);
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
        refreshContent(interview.getValue());
        bind();
        panePosition = mainSplitPane.getDividers().get(1).getPosition();
    }

    // Events:
    @FXML
    private void buttonCollapseInterviewPanelOnMouseClicked(MouseEvent mouseEvent) {
/*        if (!collapsed) {
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
        collapsed = !collapsed;*/
    }

    private void bind() { interview.addListener(interviewChangeListener); }
    public void unbind() { interview.removeListener(interviewChangeListener); }
    
    private void refreshContent(Interview newInterview) {
        if(newInterview != null) {
            textInterviewTitle.setText(newInterview.getParticipantName());
            //textInterview.setVisible(true);
            textInterviewComment.setText(newInterview.getComment());
            textInterviewComment.setVisible(true);
            borderPaneCenter.getChildren().add(TextAreaController.createTextAreaController(newInterview));
        }
        else {
            textInterviewTitle.setText(Configuration.langBundle.getString("no_interview_selected"));
            //textInterview.setVisible(false);
            textInterviewComment.setVisible(false);
            borderPaneCenter.getChildren().clear();
        }


    }
}

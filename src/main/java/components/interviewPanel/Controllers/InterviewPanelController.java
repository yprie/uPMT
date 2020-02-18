package components.interviewPanel.Controllers;

import components.interviewPanel.Models.Descripteme;
import components.interviewPanel.Models.InterviewText;
import components.interviewSelector.models.Interview;
import application.configuration.Configuration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewPanelController implements Initializable {
    // Handle the text area
    String selectedText = "";




    static private BorderPane root;
    @FXML private TextArea textInterview;
    @FXML private Text textInterviewTitle;
    @FXML private Label textInterviewComment;
    @FXML private ImageView buttonCollapseInterviewPanel;
    @FXML private BorderPane headerGrid;
    @FXML private StackPane stackForDragDrop;
    @FXML private BorderPane topBarContainerTextInterview;

    private boolean collapsed = false;
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
            root = (BorderPane) loader.load();
            return root;
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



        initializeTextArea();
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
            textInterview.setText(newInterview.getInterviewText().getText());
            textInterview.setVisible(true);
            textInterviewComment.setText(newInterview.getComment());
            textInterviewComment.setVisible(true);
        }
        else {
            textInterviewTitle.setText(Configuration.langBundle.getString("no_interview_selected"));
            textInterview.setVisible(false);
            textInterviewComment.setVisible(false);
        }
    }





    void initializeTextArea() {
        Pane paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click release on text area: add a pane over the text area
        this.textInterview.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedText = textInterview.getSelectedText().trim();
                if(!selectedText.equals(""))
                    stackForDragDrop.getChildren().add(paneDragText);
            }
        });

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                textInterview.deselect();
                stackForDragDrop.getChildren().clear();
                stackForDragDrop.getChildren().add(textInterview);
            }
        });

        // On dragging the pane
        paneDragText.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Descripteme descripteme = new Descripteme(new InterviewText(selectedText), 0, 1);
                Dragboard db = paneDragText.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(descripteme.getDataFormat(), 0);
                DragStore.setDraggable(descripteme);
                db.setContent(content);
                System.out.println("a descripteme is created and ready to drop ");
                System.out.println(descripteme);
            }
        });
    }
}

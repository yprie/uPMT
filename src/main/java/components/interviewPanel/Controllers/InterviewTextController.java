package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.Descripteme;
import models.Interview;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox toolBarAnnotation;
    @FXML private Button buttonAnnotate;
    @FXML private Button buttonDeleteAnnotate;
    @FXML private StackPane stackPaneInterview;
    @FXML private ToggleButton toggleButtonMode;

    private RichTextAreaController richTextAreaController;
    private Interview interview;
    Pane paneDragText;
    private boolean selectionActive;
    private boolean analysisMode;

    private InterviewTextController(Interview interview) {
        this.interview = interview;
        selectionActive = false;
        paneDragText = new Pane();
        analysisMode = false;
    }

    public static Node createInterviewTextController(Interview interview) {
        InterviewTextController controller = new InterviewTextController(interview);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewPanel/InterviewText.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        richTextAreaController = new RichTextAreaController(interview.getInterviewText());
        stackPaneInterview.getChildren().add(richTextAreaController.getNode());

        toggleButtonMode.setSelected(false);

        buttonAnnotate.setOnMouseClicked(event -> {
            richTextAreaController.annotate();
            hideDnDPane();
        });
        buttonDeleteAnnotate.setOnMouseClicked(event -> {
            richTextAreaController.deleteAnnotation();
            hideDnDPane();
        });

        toggleButtonMode.setOnAction(event -> {
            analysisMode = toggleButtonMode.isSelected();
            if (analysisMode) {
                richTextAreaController.switchToAnalysisMode();
            }
            else {
                richTextAreaController.switchToAnnotationMode();
            }
        });

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click release on text area: add a pane over the text area
        richTextAreaController.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!analysisMode && !richTextAreaController.getSelectedText().isEmpty()) {
                    stackPaneInterview.getChildren().add(paneDragText);
                    selectionActive = true;
                }
            }
        });

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                hideDnDPane();
            }
        });

        // On dragging the pane: start dragging from the text area
        paneDragText.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = interview.getInterviewText().getText();
                String selectedText = richTextAreaController.getSelectedText();
                int start = text.indexOf(selectedText);
                int end = start + selectedText.length();

                Descripteme descripteme = new Descripteme(interview.getInterviewText(), start, end);
                richTextAreaController.addDescripteme(descripteme); // not here, but on dropping
                Dragboard db = paneDragText.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(descripteme.getDataFormat(), 0);
                DragStore.setDraggable(descripteme);
                db.setContent(content);
            }
        });
    }

    private void hideDnDPane() {
        richTextAreaController.deselect();
        stackPaneInterview.getChildren().remove(paneDragText);
        selectionActive = false;
    }
}

package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.Annotation;
import models.Descripteme;
import models.Interview;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox toolBarAnnotation;
    @FXML private Button buttonAnnotate;
    @FXML private StackPane stackPaneInterview;

    private RichTextAreaController richTextAreaController;
    private Interview interview;

    private InterviewTextController(Interview interview) {
        this.interview = interview;
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
        richTextAreaController = new RichTextAreaController(interview);
        stackPaneInterview.getChildren().add(richTextAreaController.getNode());

        buttonAnnotate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Annotation a = richTextAreaController.annotate();
                System.out.println(a);
            }
        });

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        Pane paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click release on text area: add a pane over the text area
        stackPaneInterview.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("stackPaneInterview.setOnMouseReleased");
                if(!richTextAreaController.getSelectedText().isEmpty())
                    stackPaneInterview.getChildren().add(paneDragText);
            }
        });

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                richTextAreaController.deselect();
                stackPaneInterview.getChildren().remove(paneDragText);
            }
        });

        /**
         * start dragging from the text area :
         */
        // On dragging the pane
        paneDragText.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String text = interview.getInterviewText().getText();
                String selectedText = richTextAreaController.getSelectedText();
                int start = text.indexOf(selectedText);
                int end = start + selectedText.length();

                Descripteme descripteme = new Descripteme(interview.getInterviewText(), start, end);
                Dragboard db = paneDragText.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.put(descripteme.getDataFormat(), 0);
                DragStore.setDraggable(descripteme);
                db.setContent(content);
            }
        });
    }
}

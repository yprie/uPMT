package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.interviewPanel.Models.InterviewText;
import components.interviewSelector.models.Interview;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class TextAreaController implements Initializable {

    private Interview interview;
    private String selectedText = "";

    @FXML
    private StackPane stackForDragDrop;

    @FXML
    private TextArea textInterview;

    public static Node createTextAreaController(Interview interview) {
        TextAreaController controller = new TextAreaController(interview);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewPanel/InterviewTextArea.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TextAreaController(Interview interview) {
        this.interview = interview;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textInterview.setText(interview.getInterviewText().getText());
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {

        Pane paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click release on text area: add a pane over the text area
        textInterview.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
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

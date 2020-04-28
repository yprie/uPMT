package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ContextMenus.ContextMenuFactory;
import components.interviewPanel.ToolBar.ToolBarController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.Descripteme;
import models.Interview;
import models.InterviewText;
import utils.GlobalVariables;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox toolBarAnnotation;
    @FXML private StackPane stackPaneInterview;

    private RichTextAreaController richTextAreaController;
    private final Interview interview;
    private Pane paneDragText;

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
        Map<String, String> colors = new HashMap<>() {
            {
                put("YELLOW", "#FFDC97");
                put("RED", "#FF9797");
                put("BLUE", "#7084B0");
                put("GREEN", "#7BCF7B");
            }
        };
        ToolBarController toolBarController = ToolBarController.createToolBarController(interview.getInterviewText(), colors);
        toolBarAnnotation.getChildren().add(toolBarController.getNode());

        toolBarController.getSelectedToolProperty() // DEBUG
                .addListener(change -> System.out.println("new tool is " + change));

        richTextAreaController = new RichTextAreaController(interview.getInterviewText());
        richTextAreaController.setContextMenuFactory(new ContextMenuFactory(richTextAreaController, this));
        stackPaneInterview.getChildren().add(richTextAreaController.getNode());

        // On click release on text area: add a pane over the text area
        richTextAreaController.getUserSelection().addListener((change) -> {
            toolBarController.getSelectedToolProperty().get()
                    .getTool().handle(richTextAreaController.getUserSelection().get());
        });

        setupDragAndDrop();
        //setUpTools();

        Platform.runLater(this::initializeDescripteme);
    }

    private void initializeDescripteme() {
        ArrayList<Descripteme> descriptemes = GlobalVariables.getGlobalVariables().getAllDescripteme();
        for (Descripteme descripteme: descriptemes) {
            richTextAreaController.addDescripteme(descripteme);
        }
    }

    private void setupDragAndDrop() {
        paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(arg0 -> hideDnDPane());

        // On dragging the pane: start dragging from the text area
        paneDragText.setOnDragDetected(event -> {
            IndexRange selectedText = richTextAreaController.getSelection();

            Descripteme descripteme = new Descripteme(
                    interview.getInterviewText(),
                    selectedText.getStart(),
                    selectedText.getEnd());
            Dragboard db = paneDragText.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.put(descripteme.getDataFormat(), 0);
            DragStore.setDraggable(descripteme);
            db.setContent(content);
        });

        paneDragText.setOnDragDone(event -> {
            if (event.isAccepted()) {
                richTextAreaController.addDescripteme(DragStore.getDraggable());
                hideDnDPane();
            }
        });
    }

    private void hideDnDPane() {
        stackPaneInterview.getChildren().remove(paneDragText);
    }

    public void addPaneForDragAndDrop() {
        stackPaneInterview.getChildren().add(paneDragText);
    }

    public InterviewText getInterviewText() {
        return interview.getInterviewText();
    }
}

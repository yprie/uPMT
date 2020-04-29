package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ContextMenus.ContextMenuFactory;
import components.interviewPanel.ToolBar.CommandFactory;
import components.interviewPanel.ToolBar.ToolBarController;
import components.interviewPanel.ToolBar.tools.AnnotationTool;
import components.interviewPanel.ToolBar.tools.Controllers.AnnotationToolController;
import components.interviewPanel.ToolBar.tools.Controllers.EraserToolController;
import components.interviewPanel.ToolBar.tools.Controllers.SelectionToolController;
import components.interviewPanel.ToolBar.tools.Controllers.ToolController;
import components.interviewPanel.ToolBar.tools.EraserTool;
import components.interviewPanel.ToolBar.tools.SelectionTool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
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
import java.util.HashMap;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox hboxAnnotation;
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
        ToolBarController toolBarController = new ToolBarController();
        ToolController selectionToolController = new SelectionToolController("selection",
                new SelectionTool( "#fff", interview.getInterviewText(), new CommandFactory(this)), true);
        toolBarController.addTool(selectionToolController);
        new HashMap<String, String>() {
            {
                put("yellow", "#FFDC97");
                put("red", "#FF9797");
                put("blue", "#7084B0");
                put("green", "#7BCF7B");
            }
        }.forEach((key, value) -> {
            toolBarController.addTool(new AnnotationToolController(key,
                    new AnnotationTool(value, interview.getInterviewText())));
        });
        toolBarController.addSeparator();
        toolBarController.addTool(new EraserToolController("eraser",
                new EraserTool("#8b8b8b", interview.getInterviewText())));
        toolBarController.setSelectedToolProperty(selectionToolController);
        hboxAnnotation.getChildren().add(toolBarController);


        richTextAreaController = new RichTextAreaController(interview.getInterviewText());
        richTextAreaController.setContextMenuFactory(new ContextMenuFactory(richTextAreaController, this));
        stackPaneInterview.getChildren().add(richTextAreaController.getNode());

        // On click release on text area: add a pane over the text area
        richTextAreaController.getUserSelection().addListener((change) -> toolBarController.getSelectedToolProperty().get()
                .getTool().handle(richTextAreaController.getUserSelection().get()));

        setupDragAndDrop();

        Platform.runLater(this::initializeDescripteme);
    }

    private void initializeDescripteme() {
        GlobalVariables.getGlobalVariables().getAllDescripteme()
                .forEach(descripteme -> richTextAreaController.addDescripteme(descripteme));
    }

    private void setupDragAndDrop() {
        paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(arg0 -> hideDnDPane());
        paneDragText.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY){
                hideDnDPane();
                richTextAreaController.updateContextMenu();
            }
        });

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

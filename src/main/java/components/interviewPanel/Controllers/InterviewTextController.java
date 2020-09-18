package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ContextMenus.ContextMenuFactory;
import components.interviewPanel.ToolBar.ToolBarController;
import components.interviewPanel.ToolBar.tools.AnnotationTool;
import components.interviewPanel.ToolBar.tools.Controllers.AnnotationToolController;
import components.interviewPanel.ToolBar.tools.Controllers.EraserToolController;
import components.interviewPanel.ToolBar.tools.Controllers.SelectionToolController;
import components.interviewPanel.ToolBar.tools.Controllers.ToolController;
import components.interviewPanel.ToolBar.tools.EraserTool;
import components.interviewPanel.ToolBar.tools.SelectionTool;
import components.interviewPanel.appCommands.InterviewTextCommandFactory;
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
import models.AnnotationColor;
import models.Descripteme;
import models.Interview;
import utils.GlobalVariables;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox hboxAnnotation;
    @FXML private StackPane stackPaneInterview;
    private InterviewTextCommandFactory interviewTextCommandFactory;
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

        List<AnnotationColor> annotationColorList = new ArrayList<>();
        annotationColorList.add(new AnnotationColor("yellow", "#FFDC97"));
        annotationColorList.add(new AnnotationColor("red", "#FF9797"));
        annotationColorList.add(new AnnotationColor("blue", "#7084B0"));
        annotationColorList.add(new AnnotationColor("green", "#7BCF7B"));

        richTextAreaController = new RichTextAreaController(interview.getInterviewText(), annotationColorList);

        stackPaneInterview.getChildren().add(richTextAreaController.getNode());

        setupDragAndDrop();

        interviewTextCommandFactory = new InterviewTextCommandFactory(this,
                richTextAreaController, interview.getInterviewText());
        ContextMenuFactory contextMenuFactory = new ContextMenuFactory(interviewTextCommandFactory, annotationColorList);
        richTextAreaController.setContextMenuFactory(contextMenuFactory);

        ToolBarController toolBarController = new ToolBarController();

        annotationColorList.forEach((annotationColor) -> {
            ToolController annotationToolController = new AnnotationToolController(annotationColor.getName(),
                    new AnnotationTool(annotationColor.getHexa(), interview.getInterviewText(), interviewTextCommandFactory));
            toolBarController.addTool(annotationToolController);
        });
        toolBarController.addSeparator();
        ToolController selectionToolController = new SelectionToolController(
                "selection",
                new SelectionTool( "#fff", interview.getInterviewText(), interviewTextCommandFactory), true);
        toolBarController.addTool(selectionToolController);
        toolBarController.addTool(new EraserToolController("eraser",
                new EraserTool("#8b8b8b", interview.getInterviewText(), interviewTextCommandFactory)));
        toolBarController.setSelectedToolProperty(selectionToolController);
        hboxAnnotation.getChildren().add(toolBarController);

        // On click release on text area: add a pane over the text area
        richTextAreaController.getUserSelection().addListener((change) -> toolBarController.getSelectedToolProperty().get()
                .getTool().handle(richTextAreaController.getUserSelection().getValue()));

        Platform.runLater(() -> {
            // Initialize descriptemes
            interview.getInterviewText().getDescriptemesProperty().clear();
            GlobalVariables.getGlobalVariables().getAllDescripteme()
                    .forEach(descripteme -> {
                        interview.getInterviewText().addDescripteme(descripteme);
                    });
        });

    }

    private void setupDragAndDrop() {
        paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);

        // On click on the added pane, remove the pane
        paneDragText.setOnMouseClicked(arg0 -> hideDnDPane());
        paneDragText.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY  || event.isControlDown()){
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
}

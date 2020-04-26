package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.AnnotationColor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.Descripteme;
import models.Interview;
import utils.GlobalVariables;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    @FXML private HBox toolBarAnnotation;
    @FXML private ToggleButton buttonAnnotateYellow;
    @FXML private ToggleButton buttonAnnotateRed;
    @FXML private ToggleButton buttonAnnotateBlue;
    @FXML private ToggleButton buttonAnnotateGreen;
    @FXML private ToggleButton buttonAnnotateEraser;
    @FXML private ToggleButton buttonAnnotateSelection;
    @FXML private StackPane stackPaneInterview;

    private RichTextAreaController richTextAreaController;
    private final Interview interview;
    Pane paneDragText;

    private InterviewTextController(Interview interview) {
        this.interview = interview;
        paneDragText = new Pane();
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

        // On click release on text area: add a pane over the text area
        richTextAreaController.getUserSelection().addListener((ChangeListener) (o, oldVal, newVal) -> {
            if(!richTextAreaController.getSelectedText().isEmpty()
                    && richTextAreaController.getSelectionToolSelected()) {
                stackPaneInterview.getChildren().add(paneDragText);
            }
        });

        setupDragAndDrop();
        setUpTools();

        Platform.runLater(this::initializeDescripteme);
    }

    private void initializeDescripteme() {
        ArrayList<Descripteme> descriptemes = GlobalVariables.getGlobalVariables().getAllDescripteme();
        for (Descripteme descripteme: descriptemes) {
            richTextAreaController.addDescripteme(descripteme);
        }
    }

    private void setupDragAndDrop() {
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

    private void setUpTools() {
        buttonAnnotateYellow.setStyle("-fx-text-fill:" + AnnotationColor.YELLOW());
        buttonAnnotateRed.setStyle("-fx-text-fill:" + AnnotationColor.RED());
        buttonAnnotateBlue.setStyle("-fx-text-fill:" + AnnotationColor.BLUE());
        buttonAnnotateGreen.setStyle("-fx-text-fill:" + AnnotationColor.GREEN());
        buttonAnnotateEraser.setStyle("-fx-text-fill: #8b8b8b;-fx-background-color: white");
        buttonAnnotateSelection.setStyle("-fx-text-fill: white;-fx-background-color: black");
        buttonAnnotateSelection.setSelected(true);

        buttonAnnotateYellow.setOnMouseClicked(event -> {
            if (buttonAnnotateYellow.isSelected()) {
                richTextAreaController.setToolColorSelected(AnnotationColor.ColorYELLOW());
                deselectTools(buttonAnnotateYellow);
                buttonAnnotateYellow.setStyle("-fx-text-fill: white;-fx-background-color:" + AnnotationColor.YELLOW());
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
        buttonAnnotateRed.setOnMouseClicked(event -> {
            if (buttonAnnotateRed.isSelected()) {
                richTextAreaController.setToolColorSelected(AnnotationColor.ColorRED());
                deselectTools(buttonAnnotateRed);
                buttonAnnotateRed.setStyle("-fx-text-fill: white;-fx-background-color:" + AnnotationColor.RED());
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
        buttonAnnotateBlue.setOnMouseClicked(event -> {
            if (buttonAnnotateBlue.isSelected()) {
                richTextAreaController.setToolColorSelected(AnnotationColor.ColorBLUE());
                deselectTools(buttonAnnotateBlue);
                buttonAnnotateBlue.setStyle("-fx-text-fill: white;-fx-background-color:" + AnnotationColor.BLUE());
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
        buttonAnnotateGreen.setOnMouseClicked(event -> {
            if (buttonAnnotateGreen.isSelected()) {
                richTextAreaController.setToolColorSelected(AnnotationColor.ColorGREEN());
                deselectTools(buttonAnnotateGreen);
                buttonAnnotateGreen.setStyle("-fx-text-fill: white;-fx-background-color:" + AnnotationColor.GREEN());
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
        buttonAnnotateEraser.setOnMouseClicked(event -> {
            if (buttonAnnotateEraser.isSelected()) {
                richTextAreaController.setToolColorSelected(null);
                deselectTools(buttonAnnotateEraser);
                richTextAreaController.setEraserToolSelected(true);
                buttonAnnotateEraser.setStyle("-fx-text-fill: white;-fx-background-color: #8b8b8b");
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
        buttonAnnotateSelection.setOnMouseClicked(event -> {
            if (buttonAnnotateSelection.isSelected()) {
                richTextAreaController.setToolColorSelected(null);
                deselectTools(buttonAnnotateSelection);
                buttonAnnotateSelection.setStyle("-fx-text-fill: white;-fx-background-color: black");
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
    }

    private void deselectTools(ToggleButton selectedTool) {
        ArrayList<ToggleButton> tools = new ArrayList<>();
        tools.add(buttonAnnotateYellow);
        tools.add(buttonAnnotateRed);
        tools.add(buttonAnnotateBlue);
        tools.add(buttonAnnotateGreen);
        tools.add(buttonAnnotateEraser);
        tools.add(buttonAnnotateSelection);
        for (ToggleButton tool : tools) {
            if (tool != selectedTool) {
                tool.setSelected(false);
            }
        }
        buttonAnnotateYellow.setStyle("-fx-background-color: white;-fx-text-fill:" + AnnotationColor.YELLOW());
        buttonAnnotateRed.setStyle("-fx-background-color: white;-fx-text-fill:" + AnnotationColor.RED());
        buttonAnnotateBlue.setStyle("-fx-background-color: white;-fx-text-fill:" + AnnotationColor.BLUE());
        buttonAnnotateGreen.setStyle(";-fx-background-color: white;-fx-text-fill:" + AnnotationColor.GREEN());
        buttonAnnotateSelection.setStyle("-fx-text-fill: black;-fx-background-color: white");
        buttonAnnotateEraser.setStyle("-fx-text-fill: #8b8b8b;-fx-background-color: white");
        richTextAreaController.setEraserToolSelected(false);
    }

    private void hideDnDPane() {
        //richTextAreaController.deselect();
        stackPaneInterview.getChildren().remove(paneDragText);
    }
}

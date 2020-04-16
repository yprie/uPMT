package components.interviewPanel.Controllers;

import application.configuration.Configuration;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import models.Descripteme;
import models.Interview;
import utils.GlobalVariables;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InterviewTextController implements Initializable {

    final private String YELLOW = "#FFDC97";
    final private String RED = "#FF9797";
    final private String BLUE = "#7084B0";
    final private String GREEN = "#7BCF7B";

    @FXML private HBox toolBarAnnotation;
    @FXML private ToggleButton buttonAnnotateYellow;
    @FXML private ToggleButton buttonAnnotateRed;
    @FXML private ToggleButton buttonAnnotateBlue;
    @FXML private ToggleButton buttonAnnotateGreen;
    @FXML private ToggleButton buttonAnnotateEraser;
    @FXML private ToggleButton buttonAnnotateSelection;
    @FXML private StackPane stackPaneInterview;

    private RichTextAreaController richTextAreaController;
    private Interview interview;
    Pane paneDragText;
    private boolean analysisMode;

    private InterviewTextController(Interview interview) {
        this.interview = interview;
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

        // On click release on text area: add a pane over the text area
        richTextAreaController.getUserSelection().addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
                if(!analysisMode
                        && !richTextAreaController.getSelectedText().isEmpty()
                        && richTextAreaController.getToolColorSelected() == null) {
                    stackPaneInterview.getChildren().add(paneDragText);
                }
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
            }
        });

        paneDragText.setOnDragDone(event -> {
            if (event.isAccepted()) {
                richTextAreaController.addDescripteme(DragStore.getDraggable());
                hideDnDPane();
            }
        });
    }

    private void setUpTools() {
        buttonAnnotateYellow.setStyle("-fx-text-fill:" + YELLOW);
        buttonAnnotateRed.setStyle("-fx-text-fill:" + RED);
        buttonAnnotateBlue.setStyle("-fx-text-fill:" + BLUE);
        buttonAnnotateGreen.setStyle("-fx-text-fill:" + GREEN);
        buttonAnnotateEraser.setStyle("-fx-text-fill: #8B8B8B;");
        buttonAnnotateSelection.setStyle("-fx-text-fill: black;");
        buttonAnnotateSelection.setSelected(true);

        buttonAnnotateYellow.setOnMouseClicked(event -> {
            if (buttonAnnotateYellow.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.web(YELLOW));
                deselectTools(buttonAnnotateYellow);
                buttonAnnotateYellow.setStyle("-fx-text-fill: white;-fx-background-color:" + YELLOW);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
                buttonAnnotateYellow.setStyle("-fx-background-color: none;-fx-text-fill:" + YELLOW);
            }
        });
        buttonAnnotateRed.setOnMouseClicked(event -> {
            if (buttonAnnotateRed.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.web(RED));
                deselectTools(buttonAnnotateRed);
                buttonAnnotateRed.setStyle("-fx-text-fill: white;-fx-background-color:" + RED);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
                buttonAnnotateRed.setStyle("-fx-background-color: none;-fx-text-fill:" + RED);
            }
        });
        buttonAnnotateBlue.setOnMouseClicked(event -> {
            if (buttonAnnotateBlue.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.web(BLUE));
                deselectTools(buttonAnnotateBlue);
                buttonAnnotateBlue.setStyle("-fx-text-fill: white;-fx-background-color:" + BLUE);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
                buttonAnnotateBlue.setStyle("-fx-background-color: none;-fx-text-fill:" + BLUE);
            }
        });
        buttonAnnotateGreen.setOnMouseClicked(event -> {
            if (buttonAnnotateGreen.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.web(GREEN));
                deselectTools(buttonAnnotateGreen);
                buttonAnnotateGreen.setStyle("-fx-text-fill: white;-fx-background-color:" + GREEN);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
                buttonAnnotateGreen.setStyle(";-fx-background-color: none;-fx-text-fill:" + GREEN);
            }
        });
        buttonAnnotateEraser.setOnMouseClicked(event -> {
            if (buttonAnnotateEraser.isSelected()) {
                richTextAreaController.setToolColorSelected(null); // TODO: not null but special value
                deselectTools(buttonAnnotateEraser);
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
                buttonAnnotateYellow.setStyle("-fx-background-color: none;-fx-text-fill:" + YELLOW);
                buttonAnnotateRed.setStyle("-fx-background-color: none;-fx-text-fill:" + RED);
                buttonAnnotateBlue.setStyle("-fx-background-color: none;-fx-text-fill:" + BLUE);
                buttonAnnotateGreen.setStyle(";-fx-background-color: none;-fx-text-fill:" + GREEN);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
                buttonAnnotateSelection.setSelected(true);
            }
        });
    }

    private void deselectTools(ToggleButton slectedTool) {
        ArrayList<ToggleButton> tools = new ArrayList();
        tools.add(buttonAnnotateYellow);
        tools.add(buttonAnnotateRed);
        tools.add(buttonAnnotateBlue);
        tools.add(buttonAnnotateGreen);
        tools.add(buttonAnnotateEraser);
        tools.add(buttonAnnotateSelection);
        for (ToggleButton tool : tools) {
            if (tool != slectedTool) {
                tool.setSelected(false);
            }
        }
        buttonAnnotateYellow.setStyle("-fx-background-color: none;-fx-text-fill:" + YELLOW);
        buttonAnnotateRed.setStyle("-fx-background-color: none;-fx-text-fill:" + RED);
        buttonAnnotateBlue.setStyle("-fx-background-color: none;-fx-text-fill:" + BLUE);
        buttonAnnotateGreen.setStyle(";-fx-background-color: none;-fx-text-fill:" + GREEN);
    }

    private void hideDnDPane() {
        //richTextAreaController.deselect();
        stackPaneInterview.getChildren().remove(paneDragText);
    }
}

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

    @FXML private HBox toolBarAnnotation;
    @FXML private ToggleButton buttonAnnotateYellow;
    @FXML private ToggleButton buttonAnnotateRed;
    @FXML private ToggleButton buttonAnnotateBlue;
    //@FXML private Button buttonDeleteAnnotate;
    @FXML private StackPane stackPaneInterview;
    //@FXML private ToggleButton toggleButtonMode;

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

        //toggleButtonMode.setSelected(false);

        buttonAnnotateRed.setOnMouseClicked(event -> {
            if (buttonAnnotateRed.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.RED);
                buttonAnnotateYellow.setSelected(false);
                buttonAnnotateBlue.setSelected(false);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
            }
        });
        buttonAnnotateYellow.setOnMouseClicked(event -> {
            if (buttonAnnotateYellow.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.YELLOW);
                buttonAnnotateRed.setSelected(false);
                buttonAnnotateBlue.setSelected(false);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
            }
        });
        buttonAnnotateBlue.setOnMouseClicked(event -> {
            if (buttonAnnotateBlue.isSelected()) {
                richTextAreaController.setToolColorSelected(Color.BLUE);
                buttonAnnotateYellow.setSelected(false);
                buttonAnnotateRed.setSelected(false);
            }
            else {
                richTextAreaController.setToolColorSelected(null);
            }
        });
        /*
        buttonDeleteAnnotate.setOnMouseClicked(event -> {
            richTextAreaController.deleteAnnotation();
            hideDnDPane();
        });

         */

        /*
        toggleButtonMode.setOnAction(event -> {
            analysisMode = toggleButtonMode.isSelected();
            if (analysisMode) {
                richTextAreaController.switchToAnalysisMode();
            }
            else {
                richTextAreaController.switchToAnnotationMode();
            }
        });

         */

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

        Platform.runLater(this::initializeDescripteme);
    }

    private void initializeDescripteme() {
        ArrayList<Descripteme> descriptemes = GlobalVariables.getGlobalVariables().getAllDescriteme();
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

    private void hideDnDPane() {
        //richTextAreaController.deselect();
        stackPaneInterview.getChildren().remove(paneDragText);
    }
}

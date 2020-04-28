package components.interviewPanel.ToolBar;

import components.interviewPanel.ToolBar.tools.Controllers.AnnotationToolController;
import components.interviewPanel.ToolBar.tools.Controllers.EraserToolController;
import components.interviewPanel.ToolBar.tools.Controllers.SelectionToolController;
import components.interviewPanel.ToolBar.tools.Controllers.ToolController;
import components.interviewPanel.ToolBar.tools.AnnotationTool;
import components.interviewPanel.ToolBar.tools.EraserTool;
import components.interviewPanel.ToolBar.tools.SelectionTool;
import components.interviewPanel.ToolBar.tools.Tool;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import models.InterviewText;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ToolBarController implements Initializable {
    private final ToolBar toolBar;
    private final InterviewText interviewText;
    private final SimpleObjectProperty<ToolController> selectedTool = new SimpleObjectProperty<>();
    private final Map<String, String> colors;

    public ToolBarController(InterviewText interviewText, Map<String, String> colors) {
        this.interviewText = interviewText;
        toolBar = new ToolBar();
        this.colors = colors;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Node getNode() {
        Tool selectionTool = new SelectionTool("white", "#fff", interviewText);
        ToolController selectionToolController = new SelectionToolController("selection", selectionTool, true);
        selectionToolController.getSelectedProperty().addListener(change -> {
            selectedToolChanged(selectionToolController);
        });

        toolBar.getItems().add(ToolController.createToolController(selectionToolController));
        toolBar.getItems().add(new Separator());

        colors.forEach((key, value) -> {
            Tool tool = new AnnotationTool(key, value, interviewText);
            ToolController toolController = new AnnotationToolController(key.toLowerCase(), tool);
            toolBar.getItems().add(ToolController.createToolController(toolController));
            toolController.getSelectedProperty().addListener(change -> {
                selectedToolChanged(toolController);
            });
        });

        Tool eraserTool = new EraserTool("black", "#8b8b8b", interviewText);
        ToolController eraserToolController = new EraserToolController("eraser", eraserTool);
        eraserToolController.getSelectedProperty().addListener(change -> {
            selectedToolChanged(eraserToolController);
        });

        toolBar.getItems().add(new Separator());
        toolBar.getItems().add(ToolController.createToolController(eraserToolController));

        selectedTool.setValue(selectionToolController);

        return toolBar;
    }

    private void selectedToolChanged(ToolController toolController) {
        selectedTool.get().setIsSelected(false);
        selectedTool.setValue(toolController);
    }

    public SimpleObjectProperty<ToolController> getSelectedToolProperty() {
        return selectedTool;
    }
}

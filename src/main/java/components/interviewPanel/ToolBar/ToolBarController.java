package components.interviewPanel.ToolBar;

import components.interviewPanel.ToolBar.tools.Controllers.ToolController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class ToolBarController extends ToolBar {
    private final SimpleObjectProperty<ToolController> selectedToolProperty = new SimpleObjectProperty<>();

    public ToolBarController() {
        super();
    }

    private void selectedToolChanged(ToolController toolController) {
        selectedToolProperty.get().setIsSelected(false);
        selectedToolProperty.setValue(toolController);
    }

    public void addTool(ToolController toolController) {
        toolController.getSelectedProperty().addListener(change -> {
            selectedToolChanged(toolController);
        });
        getItems().add(toolController);
    }

    public void addSeparator() {
        getItems().add(new Separator());
    }

    public void setSelectedToolProperty(ToolController selectedTool) {
        selectedToolProperty.set(selectedTool);
    }

    public SimpleObjectProperty<ToolController> getSelectedToolProperty() {
        return selectedToolProperty;
    }


}

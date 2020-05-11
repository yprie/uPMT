package components.interviewPanel.ToolBar;

import components.interviewPanel.ToolBar.tools.Controllers.ToolController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

public class ToolBarController extends HBox {
    private final SimpleObjectProperty<ToolController> selectedToolProperty = new SimpleObjectProperty<>();

    public ToolBarController() {
        super();
        this.setMaxWidth(Double.MAX_VALUE);
        this.setSpacing(20);
        this.setPadding(new Insets(0, 20, 0, 20));
    }

    private void selectedToolChanged(ToolController toolController) {
        selectedToolProperty.get().setIsSelected(false);
        selectedToolProperty.setValue(toolController);
    }

    public void addTool(ToolController toolController) {
        toolController.getSelectedProperty().addListener(change -> {
            selectedToolChanged(toolController);
        });
        getChildren().add(toolController.getNode());
    }

    public void addSeparator() {
        getChildren().add(new Separator(Orientation.VERTICAL));
    }

    public void setSelectedToolProperty(ToolController selectedTool) {
        selectedToolProperty.set(selectedTool);
    }

    public SimpleObjectProperty<ToolController> getSelectedToolProperty() {
        return selectedToolProperty;
    }


}

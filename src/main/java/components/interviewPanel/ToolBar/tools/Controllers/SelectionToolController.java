package components.interviewPanel.ToolBar.tools.Controllers;

import components.interviewPanel.ToolBar.tools.Tool;

public class SelectionToolController extends ToolController {
    public SelectionToolController(String name, Tool tool, boolean selected) {
        super(name, tool, selected);
    }

    @Override
    protected void updateStyle() {
        if (selectedProperty.get()) {
            setStyle("-fx-background-color: black;-fx-text-fill:" + tool.getHexa());
        }
        else {
            setStyle("-fx-text-fill: black;-fx-background-color:" + tool.getHexa());
        }
    }
}

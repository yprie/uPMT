package components.interviewPanel.ToolBar.tools.Controllers;

import components.interviewPanel.ToolBar.tools.Tool;

public class EraserToolController extends ToolController {
    public EraserToolController(String name, Tool tool) {
        super(name, tool);
    }

    @Override
    protected void updateStyle() {
        if (selectedProperty.get()) {
            setStyle("-fx-text-fill: white;-fx-background-color:" + tool.getHexa());
        }
        else {
            setStyle("-fx-background-color: white;-fx-text-fill:" + tool.getHexa());
        }
    }
}

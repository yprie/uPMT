package components.interviewPanel.ToolBar.tools.Controllers;

import components.interviewPanel.ToolBar.tools.Tool;
import javafx.scene.paint.Color;

public class EraserToolController extends ToolController {
    public EraserToolController(String name, Tool tool) {
        super(name, tool);
    }

    @Override
    void initializeGraphic() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 30, 20);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeLine(0, 20, 30, 0);
    }

    @Override
    protected void SetGraphicsSelected() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 30, 20);
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(0, 20, 30, 0);
    }
}

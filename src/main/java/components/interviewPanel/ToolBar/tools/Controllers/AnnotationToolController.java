package components.interviewPanel.ToolBar.tools.Controllers;

import components.interviewPanel.ToolBar.tools.Tool;
import javafx.scene.paint.Color;

public class AnnotationToolController extends ToolController {
    public AnnotationToolController(String name, Tool tool) {
        super(name, tool);

    }

    @Override
    void initializeGraphic() {
        gc.setFill(Color.web(tool.getHexa()));
        gc.fillRect(0, 0, 30, 20);
    }
}

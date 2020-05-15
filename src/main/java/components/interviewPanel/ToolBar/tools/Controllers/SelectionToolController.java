package components.interviewPanel.ToolBar.tools.Controllers;

import components.interviewPanel.ToolBar.tools.Tool;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SelectionToolController extends ToolController {
    public SelectionToolController(String name, Tool tool, boolean selected) {
        super(name, tool, selected);

    }

    @Override
    void initializeGraphic() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 30, 20);
        gc.drawImage(new Image("/images/cursor-select-2.png"), 0, 0, 30, 20);
    }

    @Override
    protected void SetGraphicsSelected() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 30, 20);
        gc.drawImage(new Image("/images/cursor-select.png"), 0, 0, 30, 20);
    }
}

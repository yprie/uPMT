package components.interviewPanel.ToolBar.tools;

import javafx.scene.control.IndexRange;
import models.InterviewText;

public class SelectionTool extends Tool {
    public SelectionTool(String colorName, String hexa, InterviewText interviewText) {
        super(colorName, hexa, interviewText);
    }

    @Override
    public void handle(IndexRange indexRange) {

    }
}

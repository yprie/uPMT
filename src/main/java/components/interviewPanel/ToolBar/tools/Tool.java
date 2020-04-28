package components.interviewPanel.ToolBar.tools;

import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import models.InterviewText;

public abstract class Tool {
    protected String colorName;
    protected String hexa;
    protected InterviewText interviewText;

    protected Tool(String colorName, String hexa, InterviewText interviewText) {
        this.colorName = colorName;
        this.hexa = hexa;
        this.interviewText = interviewText;
    }

    public String getHexa() {
        return hexa;
    }

    public Color getColor() {
        return Color.web(hexa);
    }

    public abstract void handle(IndexRange indexRange);
}

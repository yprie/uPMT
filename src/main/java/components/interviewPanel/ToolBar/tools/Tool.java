package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.IndexRange;
import models.InterviewText;

public abstract class Tool {
    protected String hexa;
    protected InterviewText interviewText;
    protected InterviewTextCommandFactory interviewPanelCommandFactory;

    protected Tool(String hexa, InterviewText interviewText, InterviewTextCommandFactory interviewPanelCommandFactory) {
        this.hexa = hexa;
        this.interviewText = interviewText;
        this.interviewPanelCommandFactory = interviewPanelCommandFactory;
    }

    public String getHexa() {
        return hexa;
    }

    public abstract void handle(IndexRange indexRange);
}

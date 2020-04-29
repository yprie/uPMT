package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.ToolBar.CommandFactory;
import javafx.scene.control.IndexRange;
import models.InterviewText;

public class SelectionTool extends Tool {
    CommandFactory commandFactory;

    public SelectionTool(String hexa, InterviewText interviewText) {
        super(hexa, interviewText);
    }

    public SelectionTool(String hexa, InterviewText interviewText, CommandFactory commandFactory) {
        super(hexa, interviewText);
        this.commandFactory = commandFactory;
    }

    @Override
    public void handle(IndexRange indexRange) {
        if (indexRange.getStart() != indexRange.getEnd())
            commandFactory.executeCommand();
    }
}

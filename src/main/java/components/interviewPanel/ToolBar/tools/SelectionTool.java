package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.IndexRange;
import models.InterviewText;

public class SelectionTool extends Tool {
    public SelectionTool(String hexa, InterviewText interviewText, InterviewTextCommandFactory interviewPanelCommandFactory) {
        super(hexa, interviewText, interviewPanelCommandFactory);
    }

    @Override
    public void handle(IndexRange indexRange) {
        if (indexRange.getStart() != indexRange.getEnd()) {
            interviewPanelCommandFactory.getDragSelectionCommand(indexRange).execute();
        }
    }
}

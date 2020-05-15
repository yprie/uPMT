package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.IndexRange;
import models.InterviewText;

public class EraserTool extends Tool {
    public EraserTool(String hexa, InterviewText interviewText, InterviewTextCommandFactory interviewPanelCommandFactory) {
        super(hexa, interviewText, interviewPanelCommandFactory);
    }

    @Override
    public void handle(IndexRange indexRange) {
        interviewPanelCommandFactory.getEraseAnnotationCommand(indexRange).execute();
    }
}

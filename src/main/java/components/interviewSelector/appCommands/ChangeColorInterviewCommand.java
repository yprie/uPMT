package components.interviewSelector.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import components.interviewSelector.controllers.NewInterviewController;
import components.interviewSelector.modelCommands.AddInterviewCommand;
import components.interviewSelector.modelCommands.ChangeColorInterview;
import components.modelisationSpace.moment.modelCommands.ChangeColorMoment;
import models.Interview;
import models.Project;
import utils.DialogState;
import utils.command.Executable;
import utils.popups.WarningPopup;

public class ChangeColorInterviewCommand implements Executable {
    private Interview interview;
    private String newColor;
    private boolean userCommand;
    private InterviewSelectorCellController interviewSelectorCellController;
    public ChangeColorInterviewCommand(Interview interview, String newColor, InterviewSelectorCellController interviewSelectorCellController) {
        this.interview = interview;
        this.newColor = newColor;
        this.userCommand = true;
        this.interviewSelectorCellController=interviewSelectorCellController;
    }
    @Override
    public Void execute() {
        String oldColor = interview.getColor();
        HistoryManager.addCommand(new ChangeColorInterview(interview, newColor, oldColor,interviewSelectorCellController), userCommand);
        return null;
    }
}

package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import models.Interview;
import models.Project;


public class ChangeColorInterview extends ModelUserActionCommand {

    private Interview interview;
    private String oldColor;
    private String newColor;
    private InterviewSelectorCellController interviewSelectorCellController;
    public ChangeColorInterview(Interview i, String newColor, String oldColor, InterviewSelectorCellController interviewSelectorCellController) {
        this.interviewSelectorCellController=interviewSelectorCellController;
        this.interview = i;
        this.newColor = newColor;
        this.oldColor = oldColor;
    }

    @Override
    public Void execute() {
        interview.setColor(newColor);
        interviewSelectorCellController.updateColor();
        return null;
    }

    @Override
    public Void undo() {
        interview.setColor(oldColor);
        return null;
    }
}
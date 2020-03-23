package components.interviewSelector.appCommands;

import application.history.HistoryManager;
import models.Project;
import components.interviewSelector.modelCommands.AddInterviewCommand;
import components.interviewSelector.controllers.NewInterviewController;
import utils.DialogState;

public class CreateNewInterviewCommand extends InterviewSelectorCommand<Void> {

    public CreateNewInterviewCommand(Project project) {
        super(project);
    }

    @Override
    public Void execute() {
        NewInterviewController controller = NewInterviewController.createNewInterview();
        if(controller.getState() == DialogState.SUCCESS){
            HistoryManager.addCommand(new AddInterviewCommand(project, controller.getCreatedInterview()), true);
            new SelectCurrentInterviewCommand(project, controller.getCreatedInterview(), false).execute();
        }
        return null;
    }
}

package interviewSelector.appCommands;

import application.History.HistoryManager;
import application.Project.Models.Project;
import interviewSelector.modelCommands.AddInterviewCommand;
import interviewSelector.controllers.NewInterviewController;
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
            new SelectCurrentInterviewCommand(project, controller.getCreatedInterview()).execute();
        }
        return null;
    }
}

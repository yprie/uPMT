package components.interviewSelector.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.controllers.ModelisationSpaceController;
import models.Project;
import components.interviewSelector.modelCommands.AddInterviewCommand;
import components.interviewSelector.controllers.NewInterviewController;
import utils.DialogState;
import models.Interview;

public class PrintAsInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;
    private ModelisationSpaceController modelisationSpaceController;

    public PrintAsInterviewCommand(Project project, Interview interview, ModelisationSpaceController modelisationSpaceController) {

        super(project);
        this.interview= interview;
        this.modelisationSpaceController = modelisationSpaceController;
    }

    @Override
    public Void execute() {
        System.out.println("test");
        HistoryManager.addCommand(new components.interviewSelector.modelCommands.PrintAsInterviewCommand(project, interview), false);

        this.modelisationSpaceController.TakeSnapshot();

        return null;
    }
}

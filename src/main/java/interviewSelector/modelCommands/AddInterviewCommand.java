package interviewSelector.modelCommands;

import application.History.ModelUserActionCommand;
import application.Project.Models.Project;
import interviewSelector.Models.Interview;
import interviewSelector.appCommands.SelectCurrentInterviewCommand;


public class AddInterviewCommand extends ModelUserActionCommand<Void, Void> {

    private Project project;
    private Interview interview;
    private Interview previousSelectedInterview;

    public AddInterviewCommand(Project p, Interview i) {
        this.project = p;
        this.interview = i;
        this.previousSelectedInterview = p.getSelectedInterview();
    }

    @Override
    public Void execute() {
        project.addInterview(interview);
        return null;
    }

    @Override
    public Void undo() {
        project.removeInterview(interview);
        return null;
    }
}

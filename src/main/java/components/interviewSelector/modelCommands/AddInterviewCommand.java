package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import models.Project;
import models.Interview;


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

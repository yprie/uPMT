package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import models.Project;
import models.Interview;

public class SelectCurrentInterviewCommand extends ModelUserActionCommand<Void, Void> {

    private Project project;
    private Interview previousInterview;
    private Interview interview;

    public SelectCurrentInterviewCommand(Project project, Interview i) {
        this.project = project;
        this.previousInterview = project.getSelectedInterview();
        this.interview = i;
    }

    @Override
    public Void execute() {
        project.setSelectedInterview(interview);
        return null;
    }

    @Override
    public Void undo() {
        project.setSelectedInterview(previousInterview);
        return null;
    }
}

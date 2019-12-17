package interviewSelector.modelCommands;

import application.History.ModelUserActionCommand;
import application.Project.Models.Project;
import interviewSelector.Models.Interview;
import utils.Command.Executable;

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

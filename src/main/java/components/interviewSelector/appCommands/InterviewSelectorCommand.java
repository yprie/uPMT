package components.interviewSelector.appCommands;

import application.project.models.Project;
import utils.command.Executable;

public abstract class InterviewSelectorCommand<ExecuteResult> implements Executable<ExecuteResult> {

    protected Project project;
    public InterviewSelectorCommand(Project project) { this.project = project; }

}

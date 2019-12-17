package interviewSelector.appCommands;

import application.Project.Models.Project;
import utils.Command.Executable;

public abstract class InterviewSelectorCommand<ExecuteResult> implements Executable<ExecuteResult> {

    protected Project project;
    public InterviewSelectorCommand(Project project) { this.project = project; }

}

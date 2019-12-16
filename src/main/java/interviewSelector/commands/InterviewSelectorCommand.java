package interviewSelector.commands;

import application.History.ModelUserActionCommand;
import application.Project.Models.Project;
import utils.Command.Executable;

public abstract class InterviewSelectorCommand<ExecuteResult, UndoResult> extends ModelUserActionCommand<ExecuteResult, UndoResult> {

    protected Project project;
    public InterviewSelectorCommand(Project project) { this.project = project; }

}

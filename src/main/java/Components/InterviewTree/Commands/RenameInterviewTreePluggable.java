package Components.InterviewTree.Commands;

import Components.InterviewTree.InterviewTreePluggable;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import utils.ReactiveTree.Commands.RenameReactiveTreePluggableCommand;

public class RenameInterviewTreePluggable extends RenameReactiveTreePluggableCommand {

    InterviewTreePluggable element;
    private boolean mustBeRenamed;

    public RenameInterviewTreePluggable(InterviewTreePluggable element, String newName) {
        super(element, newName);
        this.element = element;
        this.mustBeRenamed = element.mustBeRenamed();
    }

    @Override
    public Void execute() {
        super.execute();
        if(mustBeRenamed)
            element.setMustBeRenamed(false);
        return null;
    }

    @Override
    public Void undo() {
        if(mustBeRenamed)
            element.setMustBeRenamed(true);
        super.undo();
        return null;
    }
}

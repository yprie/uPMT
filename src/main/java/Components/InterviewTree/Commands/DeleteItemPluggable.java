package Components.InterviewTree.Commands;

import Components.InterviewTree.InterviewTreePluggable;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import application.History.ModelUserActionCommand;

public class DeleteItemPluggable extends ModelUserActionCommand<Void, Void> {

    private InterviewTreePluggable parent;
    private InterviewTreePluggable element;

    public DeleteItemPluggable(InterviewTreePluggable parent, InterviewTreePluggable element) {
        this.parent = parent;
        this.element = element;
    }

    @Override
    public Void execute() {
        parent.removeChild(element);
        return null;
    }

    @Override
    public Void undo() {
        parent.addChild(element);
        return null;
    }
}

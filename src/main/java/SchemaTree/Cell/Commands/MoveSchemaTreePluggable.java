package SchemaTree.Cell.Commands;

import ApplicationHistory.ICommand;
import SchemaTree.Cell.Models.SchemaTreePluggable;

public class MoveSchemaTreePluggable extends ICommand<Void, Void> {

    SchemaTreePluggable oldParent;
    SchemaTreePluggable newParent;
    SchemaTreePluggable element;

    public MoveSchemaTreePluggable(SchemaTreePluggable oldParent, SchemaTreePluggable newParent, SchemaTreePluggable element) {
        this.oldParent = oldParent;
        this.newParent = newParent;
        this.element = element;
    }

    @Override
    public Void execute() {
        move(oldParent, newParent, element);
        return null;
    }

    @Override
    public Void unexecute() {
        move(newParent, oldParent, element);
        return null;
    }

    private void move(SchemaTreePluggable source, SchemaTreePluggable target, SchemaTreePluggable element) {
        source.removeChild(element);
        target.addChild(element);
    }
}

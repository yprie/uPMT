package SchemaTree.Cell.Commands;

import ApplicationHistory.ICommand;
import SchemaTree.Cell.SchemaTreePluggable;

public class RemoveSchemaTreePluggable extends ICommand<Void, Void> {

    private SchemaTreePluggable parent;
    private SchemaTreePluggable element;

    public RemoveSchemaTreePluggable(SchemaTreePluggable parent, SchemaTreePluggable element) {
        this.parent = parent;
        this.element = element;
    }

    @Override
    public Void execute() {
        parent.removeChild(element);
        return null;
    }

    @Override
    public Void unexecute() {
        parent.addChild(element);
        return null;
    }
}

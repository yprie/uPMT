package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import components.schemaTree.Cell.SchemaTreePluggable;

public class PermuteSchemaTreePluggable extends ModelUserActionCommand<Void, Void> {

    int oldIndex;
    int newIndex;
    SchemaTreePluggable element;
    SchemaTreePluggable parent;

    public PermuteSchemaTreePluggable(SchemaTreePluggable parent, int oldIndex, int newIndex, SchemaTreePluggable element) {
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.element = element;
        this.parent = parent;
    }

    @Override
    public Void execute() {
        move(newIndex);
        return null;
    }

    @Override
    public Void undo() {
        move(oldIndex);
        return null;
    }

    private void move(int index) {
        this.parent.removeChild(this.element);
        this.parent.addChildAt(this.element, index);
    }
}

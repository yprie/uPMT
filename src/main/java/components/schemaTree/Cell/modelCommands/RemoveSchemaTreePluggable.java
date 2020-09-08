package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import components.schemaTree.Cell.SchemaTreePluggable;
import utils.removable.IRemovable;

public class RemoveSchemaTreePluggable<E extends SchemaTreePluggable&IRemovable> extends ModelUserActionCommand<Void, Void> {

    private SchemaTreePluggable parent;
    private E element;
    private int element_index;

    public RemoveSchemaTreePluggable(SchemaTreePluggable parent, E element) {
        this.parent = parent;
        this.element = element;
    }

    @Override
    public Void execute() {
        element_index = parent.getChildIndex(element);
        parent.removeChild(element);
        element.setExists(false);
        return null;
    }

    @Override
    public Void undo() {
        parent.addChildAt(element, element_index);
        element.setExists(true);
        return null;
    }
}

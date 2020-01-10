package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import components.schemaTree.Cell.SchemaTreePluggable;
import utils.removable.IRemovable;

public class RemoveSchemaTreePluggable<E extends SchemaTreePluggable&IRemovable> extends ModelUserActionCommand<Void, Void> {

    private SchemaTreePluggable parent;
    private E element;

    public RemoveSchemaTreePluggable(SchemaTreePluggable parent, E element) {
        this.parent = parent;
        this.element = element;
    }

    @Override
    public Void execute() {
        parent.removeChild(element);
        element.existsProperty().setValue(false);
        return null;
    }

    @Override
    public Void undo() {
        element.existsProperty().setValue(true);
        parent.addChild(element);
        return null;
    }
}

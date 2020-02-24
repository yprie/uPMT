package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import components.schemaTree.Cell.SchemaTreePluggable;

public class MoveSchemaTreePluggable extends ModelUserActionCommand<Void, Void> {

    private SchemaTreePluggable oldParent;
    private SchemaTreePluggable newParent;
    private SchemaTreePluggable element;
    private int oldElementIndex;
    private int newElementIndex;

    public MoveSchemaTreePluggable(SchemaTreePluggable oldParent, SchemaTreePluggable newParent,
                                   SchemaTreePluggable element, int newElementIndex) {
        this.oldParent = oldParent;
        this.newParent = newParent;
        this.element = element;
        this.newElementIndex = newElementIndex;
    }

    @Override
    public Void execute() {
        oldElementIndex = oldParent.getChildIndex(element);
        move(oldParent, newParent, element, newElementIndex);
        return null;
    }

    @Override
    public Void undo() {
        move(newParent, oldParent, element, this.oldElementIndex);
        return null;
    }

    private void move(SchemaTreePluggable source, SchemaTreePluggable target, SchemaTreePluggable element, int elementIndex) {
        source.removeChild(element);
        if (elementIndex == -1) {
            target.addChild(element);
        }
        else{
            target.addChildAt(element, elementIndex);
        }
    }
}

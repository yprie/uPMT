package SchemaTree.Cell.Commands;

import ApplicationHistory.ICommand;
import SchemaTree.Cell.Models.SchemaTreePluggable;

public class AddSchemaTreePluggable extends ICommand<Void, Void> {

    private SchemaTreePluggable parent;
    private SchemaTreePluggable element;
    private boolean shouldElementAlreadyBeRenamed;
    private boolean shouldElementBeRenamed;

    public AddSchemaTreePluggable(SchemaTreePluggable parent, SchemaTreePluggable element, boolean shouldElementBeRenamed) {
        this.parent = parent;
        this.element = element;
        this.shouldElementAlreadyBeRenamed = element.mustBeRenamed();
        this.shouldElementBeRenamed = shouldElementBeRenamed;
    }

    @Override
    public Void execute() {
        parent.addChild(element);
        if(shouldElementBeRenamed)
            element.setMustBeRenamed(true);
        return null;
    }

    @Override
    public Void unexecute() {
        element.setMustBeRenamed(shouldElementAlreadyBeRenamed);
        parent.removeChild(element);
        return null;
    }

}

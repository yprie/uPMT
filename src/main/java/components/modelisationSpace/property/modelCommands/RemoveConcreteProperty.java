package components.modelisationSpace.property.modelCommands;

import application.history.ModelUserActionCommand;
import models.ConcreteCategory;
import models.ConcreteProperty;

public class RemoveConcreteProperty extends ModelUserActionCommand<Void, Void> {

    private ConcreteCategory parent;
    private ConcreteProperty property;
    private int index;

    public RemoveConcreteProperty(ConcreteCategory parent, ConcreteProperty p) {
        this.parent = parent;
        this.property = p;
        this.index = parent.indexOfConcreteProperty(p);
    }

    @Override
    public Void execute() {
        parent.removeConcreteProperty(property);
        return null;
    }

    @Override
    public Void undo() {
        parent.addConcreteProperty(index, property);
        return null;
    }
}

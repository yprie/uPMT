package components.modelisationSpace.property.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.property.model.ConcreteProperty;

public class AddConcreteProperty extends ModelUserActionCommand<Void, Void> {

    private ConcreteCategory parent;
    private ConcreteProperty property;

    public AddConcreteProperty(ConcreteCategory parent, ConcreteProperty p) {
        this.parent = parent;
        this.property = p;
    }

    @Override
    public Void execute() {
        parent.addConcreteProperty(property);
        return null;
    }

    @Override
    public Void undo() {
        parent.removeConcreteProperty(property);
        return null;
    }
}

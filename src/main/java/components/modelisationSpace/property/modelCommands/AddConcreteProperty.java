package components.modelisationSpace.property.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.property.model.ConcreteProperty;

public class AddConcreteProperty extends ModelUserActionCommand<Void, Void> {

    private ConcreteCategory parent;
    private ConcreteProperty property;
    int index;

    public AddConcreteProperty(ConcreteCategory parent, ConcreteProperty p) {
        this.parent = parent;
        this.property = p;
        this.index = -1;
    }

    public AddConcreteProperty(ConcreteCategory parent, ConcreteProperty p, int index) {
        this.parent = parent;
        this.property = p;
        this.index = index;
    }

    @Override
    public Void execute() {
        if(index == -1)
            parent.addConcreteProperty(property);
        else
            parent.addConcreteProperty(index, property);
        return null;
    }

    @Override
    public Void undo() {
        parent.removeConcreteProperty(property);
        return null;
    }
}

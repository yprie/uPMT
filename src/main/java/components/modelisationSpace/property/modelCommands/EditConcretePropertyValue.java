package components.modelisationSpace.property.modelCommands;

import application.history.ModelUserActionCommand;
import models.ConcreteProperty;

public class EditConcretePropertyValue extends ModelUserActionCommand<Void, Void> {

    private ConcreteProperty property;
    private String newValue;
    private String oldValue;

    public EditConcretePropertyValue(ConcreteProperty property, String newValue) {
        this.property = property;
        this.newValue = newValue;
    }

    @Override
    public Void execute() {
        oldValue = property.getName();
        property.setValue(newValue);
        return null;
    }

    @Override
    public Void undo() {
        property.setValue(oldValue);
        return null;
    }
}

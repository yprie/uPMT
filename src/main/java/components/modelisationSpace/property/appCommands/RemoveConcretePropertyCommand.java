package components.modelisationSpace.property.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.property.model.ConcreteProperty;
import components.modelisationSpace.property.modelCommands.RemoveConcreteProperty;
import utils.command.Executable;

public class RemoveConcretePropertyCommand implements Executable<Void> {

    private ConcreteCategory parent;
    private ConcreteProperty property;

    public RemoveConcretePropertyCommand(ConcreteCategory parent, ConcreteProperty property) {
        this.parent = parent;
        this.property = property;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveConcreteProperty(parent, property), false);
        return null;
    }
}

package components.modelisationSpace.property.appCommands;

import application.history.HistoryManager;
import models.ConcreteCategory;
import models.ConcreteProperty;
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

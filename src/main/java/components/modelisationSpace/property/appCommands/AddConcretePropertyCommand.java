package components.modelisationSpace.property.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.property.model.ConcreteProperty;
import components.modelisationSpace.property.modelCommands.AddConcreteProperty;
import utils.command.Executable;

public class AddConcretePropertyCommand implements Executable<Void> {

    private ConcreteCategory category;
    private ConcreteProperty property;

    public AddConcretePropertyCommand(ConcreteCategory category, ConcreteProperty property) {
        this.category = category;
        this.property = property;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new AddConcreteProperty(category, property), false);
        return null;
    }
}

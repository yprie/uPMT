package components.modelisationSpace.property.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.property.model.ConcreteProperty;
import components.modelisationSpace.property.modelCommands.AddConcreteProperty;
import utils.command.Executable;

public class AddConcretePropertyCommand implements Executable<Void> {

    private ConcreteCategory category;
    private ConcreteProperty property;
    private int index;

    public AddConcretePropertyCommand(ConcreteCategory category, ConcreteProperty property) {
        this.category = category;
        this.property = property;
        index = -1;
    }

    public AddConcretePropertyCommand(ConcreteCategory category, ConcreteProperty property, int index) {
        this.category = category;
        this.property = property;
        this.index = index;
    }

    @Override
    public Void execute() {
        if(index == -1)
            HistoryManager.addCommand(new AddConcreteProperty(category, property), false);
        else
            HistoryManager.addCommand(new AddConcreteProperty(category, property, index), false);
        return null;
    }
}

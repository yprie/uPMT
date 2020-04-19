package components.modelisationSpace.category.appCommands;

import application.history.HistoryManager;
import models.ConcreteCategory;
import components.modelisationSpace.category.modelCommands.AddConcreteCategory;
import models.Moment;
import utils.command.Executable;

public class AddConcreteCategoryCommand implements Executable<Void> {

    private Moment parent;
    private ConcreteCategory category;
    private boolean userCommand;

    public AddConcreteCategoryCommand(Moment parent, ConcreteCategory category, boolean userCommand) {
        this.parent = parent;
        this.category = category;
        this.userCommand = userCommand;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new AddConcreteCategory(parent, category), userCommand);
        return null;
    }
}
package components.modelisationSpace.category.appCommands;

import application.history.HistoryManager;
import models.ConcreteCategory;
import components.modelisationSpace.category.modelCommands.RemoveConcreteCategory;
import models.Moment;
import utils.command.Executable;

public class RemoveConcreteCategoryCommand implements Executable<Void> {

    private Moment parent;
    private ConcreteCategory category;
    private boolean userCommand;

    public RemoveConcreteCategoryCommand(Moment parent, ConcreteCategory c, boolean userCommand) {
        this.parent = parent;
        this.category = c;
        this.userCommand = userCommand;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveConcreteCategory(parent, category), userCommand);
        return null;
    }
}

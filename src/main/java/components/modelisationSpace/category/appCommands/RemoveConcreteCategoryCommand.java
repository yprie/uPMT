package components.modelisationSpace.category.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.category.modelCommands.RemoveConcreteCategory;
import components.modelisationSpace.moment.model.Moment;
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

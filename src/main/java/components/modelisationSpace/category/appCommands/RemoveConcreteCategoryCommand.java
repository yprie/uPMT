package components.modelisationSpace.category.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import models.ConcreteCategory;
import components.modelisationSpace.category.modelCommands.RemoveConcreteCategory;
import models.Moment;
import utils.command.Executable;

public class RemoveConcreteCategoryCommand implements Executable<Void> {

    private Moment parent;
    private ConcreteCategory category;
    private boolean userCommand;
    private ModelisationSpaceHookNotifier hooksNotifier;
    private ConcreteCategoryController controller;

    public RemoveConcreteCategoryCommand(ModelisationSpaceHookNotifier hooksNotifier, Moment parent, ConcreteCategory c, ConcreteCategoryController controller, boolean userCommand) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.category = c;
        this.userCommand = userCommand;
        this.controller = controller;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveConcreteCategory(parent, category, controller), userCommand);
        hooksNotifier.notifyConcreteCategoryRemoved(category);
        return null;
    }
}

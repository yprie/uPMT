package components.modelisationSpace.category.appCommands;

import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import models.ConcreteCategory;
import components.modelisationSpace.category.modelCommands.AddConcreteCategory;
import models.Moment;
import models.SchemaCategory;
import utils.command.Executable;

public class AddConcreteCategoryCommand implements Executable<Void> {

    private ModelisationSpaceHookNotifier hookNotifier;
    private Moment parent;
    private ConcreteCategory category;
    private boolean userCommand;

    @Override
    public Void execute() {
        AddConcreteCategory cmd = new AddConcreteCategory(parent, category);
        if(hookNotifier != null) {
            cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> hookNotifier.notifyConcreteCategoryAdded(category));
            cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> hookNotifier.notifyConcreteCategoryRemoved(category));
        }
        HistoryManager.addCommand(cmd, userCommand);

        return null;
    }

    public AddConcreteCategoryCommand(ModelisationSpaceHookNotifier hookNotifier, Moment parent, SchemaCategory category, boolean userCommand) {
        this.hookNotifier = hookNotifier;
        this.parent = parent;
        this.category = new ConcreteCategory(category);
        this.userCommand = userCommand;
    }

    public AddConcreteCategoryCommand(ModelisationSpaceHookNotifier hookNotifier, Moment parent, ConcreteCategory category, boolean userCommand) {
        this.hookNotifier = hookNotifier;
        this.parent = parent;
        this.category = category;
        this.userCommand = userCommand;
    }
}

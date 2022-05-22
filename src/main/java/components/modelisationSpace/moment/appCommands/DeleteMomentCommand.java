package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import models.Moment;
import models.RootMoment;
import components.modelisationSpace.moment.modelCommands.RemoveSubMoment;
import utils.command.Executable;

public class DeleteMomentCommand implements Executable<Void> {

    ModelisationSpaceHookNotifier hooksNotifier;
    RootMoment parent;
    Moment moment;
    boolean userCommand;

    public DeleteMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment m, boolean userCommand) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.moment = m;
        this.userCommand = userCommand;
    }

    @Override
    public Void execute() {
        RemoveSubMoment cmd = new RemoveSubMoment(parent, moment);
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> hooksNotifier.notifyMomentRemoved(moment));
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> hooksNotifier.notifyMomentAdded(moment));
        HistoryManager.addCommand(cmd, userCommand);

        return null;
    }
}

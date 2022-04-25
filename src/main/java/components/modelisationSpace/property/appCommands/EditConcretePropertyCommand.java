package components.modelisationSpace.property.appCommands;

import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.property.modelCommands.EditConcretePropertyValue;
import models.ConcreteProperty;
import utils.command.Executable;

public class EditConcretePropertyCommand implements Executable<Void> {
    private ModelisationSpaceHookNotifier hooksNotifier;
    private ConcreteProperty property;
    private String newValue;
    private boolean userCommand;

    public EditConcretePropertyCommand(ModelisationSpaceHookNotifier hooksNotifier, ConcreteProperty property, String newValue, boolean userCommand) {
        this.hooksNotifier = hooksNotifier;
        this.property = property;
        this.newValue = newValue;
        this.userCommand = userCommand;
    }

    @Override
    public Void execute() {
        EditConcretePropertyValue cmd = new EditConcretePropertyValue(property, newValue);

        if(hooksNotifier != null) {
            cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> hooksNotifier.notifyConcretePropertyValueChanged(property.getValue(), property));
            cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> hooksNotifier.notifyConcretePropertyValueChanged(newValue, property));
        }

        HistoryManager.addCommand(cmd, userCommand);
        return null;
    }
}



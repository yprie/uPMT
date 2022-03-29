package components.modelisationSpace.category.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.justification.appCommands.AddDescriptemeCommand;
import components.modelisationSpace.justification.appCommands.RemoveDescriptemeCommand;
import components.modelisationSpace.property.modelCommands.EditConcretePropertyValue;
import models.ConcreteCategory;
import models.ConcreteProperty;
import utils.DialogState;
import utils.command.Executable;
import utils.popups.MergerPopup;

public class MergeConcreteCategoryCommand implements Executable<Void> {

    private ModelisationSpaceHookNotifier hookNotifier;
    private ConcreteCategory destinationCategory;
    private ConcreteCategory sourceCategory;
    private boolean userCommand;
    private boolean confirmationNeeded;

    public MergeConcreteCategoryCommand(ModelisationSpaceHookNotifier hookNotifier, ConcreteCategory destinationCategory, ConcreteCategory sourceCategory, boolean userCommand) {
        this.hookNotifier = hookNotifier;
        this.destinationCategory = destinationCategory;
        this.sourceCategory = sourceCategory;
        this.userCommand = userCommand;
        this.confirmationNeeded = true;
    }

    public MergeConcreteCategoryCommand(ModelisationSpaceHookNotifier hookNotifier, ConcreteCategory destinationCategory, ConcreteCategory sourceCategory, boolean userCommand, boolean confirmationNeeded) {
        this.hookNotifier = hookNotifier;
        this.destinationCategory = destinationCategory;
        this.sourceCategory = sourceCategory;
        this.userCommand = userCommand;
        this.confirmationNeeded = confirmationNeeded;
    }

    public StringBuilder buildMessage() {
        StringBuilder message = new StringBuilder();
        sourceCategory.propertiesProperty().forEach(sourceProperty -> {
            int sourcePropertyIndex = sourceCategory.propertiesProperty().indexOf(sourceProperty);
            String destinationPropertyValue = destinationCategory.propertiesProperty().get(sourcePropertyIndex).getValue();
            if (destinationPropertyValue.equals("")) destinationPropertyValue = "''";

            message.append(sourceProperty.getName()).append(" : ").append(destinationPropertyValue).append(" -> ");
            if (sourceProperty.getValue().equals("")) {
                message.append(destinationPropertyValue);
            } else {
                message.append(sourceProperty.getValue());
            }
            message.append("\n");
        });
        return message;
    }

    private boolean confirmationMessage() {
        String message = buildMessage() +
                Configuration.langBundle.getString("merge_confirmation");

        MergerPopup mp = MergerPopup.display(message, sourceCategory.getName());

        return mp.getState() == DialogState.SUCCESS;

    }
    @Override
    public Void execute() {

        //confirm the merge
        if(confirmationNeeded) {
            if (!confirmationMessage()) return null;
        }

        //Merge command
        sourceCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
            new AddDescriptemeCommand(destinationCategory.getJustification(), descripteme, userCommand).execute();
            if (userCommand) userCommand = false;
        });


        sourceCategory.propertiesProperty().forEach(sourceProperty -> {
            if (sourceProperty.getValue().equals("")) return; //skip only this lambda function

            int sourcePropertyIndex = sourceCategory.propertiesProperty().indexOf(sourceProperty);
            ConcreteProperty destinationProperty = destinationCategory.propertiesProperty().get(sourcePropertyIndex);

            //change the property value
            EditConcretePropertyValue cmd = new EditConcretePropertyValue(destinationProperty, sourceProperty.getValue());
            if(hookNotifier != null) {
                cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> hookNotifier.notifyConcretePropertyValueChanged(destinationProperty.getValue(), destinationProperty));
                cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> hookNotifier.notifyConcretePropertyValueChanged(sourceProperty.getValue(), destinationProperty));
            }
            HistoryManager.addCommand(cmd, userCommand);
            if (userCommand) userCommand = false;


            //delete all old descriptems
            destinationProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                RemoveDescriptemeCommand cmd2 = new RemoveDescriptemeCommand(destinationProperty.getJustification(), descripteme);
                cmd2.isNotUserAction();
                cmd2.execute();
            });

            //copy all new descriptems
            sourceProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                AddDescriptemeCommand cmd2 = new AddDescriptemeCommand(destinationProperty.getJustification(), descripteme, false);
                cmd2.execute();
            });
        });

        return null;
    }
}

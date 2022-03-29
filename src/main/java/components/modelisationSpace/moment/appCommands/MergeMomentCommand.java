package components.modelisationSpace.moment.appCommands;

import application.configuration.Configuration;
import components.modelisationSpace.category.appCommands.AddConcreteCategoryCommand;
import components.modelisationSpace.category.appCommands.MergeConcreteCategoryCommand;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.justification.appCommands.AddDescriptemeCommand;
import models.ConcreteCategory;
import models.Moment;
import utils.DialogState;
import utils.command.Executable;
import utils.popups.MergerPopup;
import utils.popups.WarningPopup;

import java.util.concurrent.atomic.AtomicBoolean;

public class MergeMomentCommand implements Executable<Void> {
    private ModelisationSpaceHookNotifier hookNotifier;
    private Moment destinationMoment;
    private Moment sourceMoment;
    private boolean userCommand;



    public MergeMomentCommand(ModelisationSpaceHookNotifier hookNotifier, Moment destinationMoment, Moment sourceMoment, boolean userCommand) {
        this.hookNotifier = hookNotifier;
        this.destinationMoment = destinationMoment;
        this.sourceMoment = sourceMoment;
        this.userCommand = userCommand;
    }

    private boolean confirmationMessage() {
        StringBuilder message = new StringBuilder();
        AtomicBoolean confirmationNeeded = new AtomicBoolean(false);

        sourceMoment.concreteCategoriesProperty().forEach(category -> {
            if (!destinationMoment.hadThisCategory(category)) return;   //return of the lambda function, not global fonction
            confirmationNeeded.set(true);
            ConcreteCategory destinationCategory = destinationMoment.getCategory(category);

            //message for category to merge
            message.append(category.getName()).append(" :\n");
            message.append(new MergeConcreteCategoryCommand(null, destinationCategory, category, false).buildMessage());
            message.append("\n");
        });

        if(!confirmationNeeded.get()) {
            return true;
        }

        message.append(Configuration.langBundle.getString("merge_confirmation"));
        MergerPopup mp = MergerPopup.display(message.toString(), sourceMoment.getName());
        return mp.getState() == DialogState.SUCCESS;

    }

    @Override
    public Void execute() {
        if (!sourceMoment.momentsProperty().isEmpty()) {
            WarningPopup.display(Configuration.langBundle.getString("merge_warning"));
            return null;
        }

        //show message and wait user to confirm the merge
        if (!confirmationMessage()) return null;


        //Copy descriptemes
        sourceMoment.getJustification().descriptemesProperty().forEach(descripteme -> {
            new AddDescriptemeCommand(destinationMoment.getJustification(), descripteme, userCommand).execute();
            if (userCommand) userCommand = false;
        });


        //Copy categories
        sourceMoment.concreteCategoriesProperty().forEach(category -> {
            if (!destinationMoment.hadThisCategory(category)) {
                //add category
                new AddConcreteCategoryCommand(hookNotifier, destinationMoment, category, userCommand).execute();
            }
            else {
                //Merge category
                ConcreteCategory destinationCC = destinationMoment.getCategory(category);
                new MergeConcreteCategoryCommand(hookNotifier, destinationCC, category, userCommand, false).execute();
            }
            if (userCommand) userCommand = false;
        });

        //Delete moment
        new DeleteMomentCommand(hookNotifier,sourceMoment.getParent(), sourceMoment, userCommand).execute();

        return null;
    }
}

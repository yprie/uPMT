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

import java.util.ArrayList;


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

        //information part
        message.append(Configuration.langBundle.getString("merge_information_p1"))
                .append(" \"").append(sourceMoment.getName()).append("\" ")
                .append(Configuration.langBundle.getString("merge_information_p2"))
                .append(" \"").append(destinationMoment.getName()).append("\".\n\n");

        //add part
        message.append(Configuration.langBundle.getString("merge_add_info"))
                .append(" \"").append(destinationMoment.getName()).append("\" :\n");

        message.append("\t- ").append(Configuration.langBundle.getString("merge_add_moment_descriptem"))
                .append(" \"").append(sourceMoment.getName()).append("\"\n");


        ArrayList<String> categoryNames = new ArrayList<>();
        sourceMoment.concreteCategoriesProperty().forEach(category -> {
            if (!destinationMoment.hadThisCategory(category)) {
                categoryNames.add(category.getName());
            }
        });

        if (!categoryNames.isEmpty()){
            message.append("\t- ").append(Configuration.langBundle.getString("merge_add_category_p1"))
                    .append(" \"").append(destinationMoment.getName()).append("\" ")
                    .append(Configuration.langBundle.getString("merge_add_category_p2"))
                    .append(" \"").append(sourceMoment.getName()).append("\" ");

            String ctgNames = categoryNames.toString();
            ctgNames = "(" + ctgNames.substring(1, ctgNames.length()-1) + ")";

            message.append(ctgNames)
                    .append(Configuration.langBundle.getString("merge_add_category_p3")).append("\n\n");
        }


        //merge part
        StringBuilder allCategoriesMessage = new StringBuilder();

        sourceMoment.concreteCategoriesProperty().forEach(category -> {
            if (!destinationMoment.hadThisCategory(category)) return;

            ConcreteCategory destinationCategory = destinationMoment.getCategory(category);

            StringBuilder cc = new MergeConcreteCategoryCommand(null, destinationCategory, category, false).buildMessage();
            //message for category to merge
            if (!cc.isEmpty()){
                allCategoriesMessage.append("\t- ").append(category.getName()).append(" :\n")
                        .append(cc);
            }

        });

        if (!allCategoriesMessage.isEmpty()) {
            message.append(Configuration.langBundle.getString("merge_merge_category")).append(" :\n");
            message.append(allCategoriesMessage).append('\n');
        }

        //end part
        message.append(Configuration.langBundle.getString("merge_confirmation"));
        MergerPopup mp = MergerPopup.display(message.toString());
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

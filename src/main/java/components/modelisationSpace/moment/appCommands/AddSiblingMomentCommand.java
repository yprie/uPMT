package components.modelisationSpace.moment.appCommands;


import application.configuration.Configuration;
import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.category.appCommands.AddConcreteCategoryCommand;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import models.ConcreteCategory;
import models.Moment;
import models.RootMoment;
import components.modelisationSpace.moment.modelCommands.AddSubMoment;
import models.SchemaCategory;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.command.Executable;
import utils.popups.TextEntryController;

public class AddSiblingMomentCommand implements Executable<Void> {

    private RootMoment parent;
    private Moment newMoment;
    private SchemaCategory schemaCategory;
    private ConcreteCategory concreteCategory;
    int index = -1;
    private ModelisationSpaceHookNotifier hooksNotifier;

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment, ConcreteCategory concreteCategory) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        this.concreteCategory = concreteCategory;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment, ConcreteCategory concreteCategory, int index) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        this.index = index;
        this.concreteCategory = concreteCategory;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment root, Moment newMoment, SchemaCategory schemaCategory, Moment parent) {
        this.hooksNotifier = hooksNotifier;
        this.parent = root;
        this.newMoment = newMoment;
        this.schemaCategory = schemaCategory;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment root, Moment newMoment, SchemaCategory schemaCategory, Moment parent, int index) {
        this.hooksNotifier = hooksNotifier;
        this.parent = root;
        this.newMoment = newMoment;
        this.schemaCategory = schemaCategory;
        this.index = index;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment, int index) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        this.index = index;
        concreteCategory = null;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        concreteCategory = null;
    }

    @Override
    public Void execute() {
        String name = Configuration.langBundle.getString("new_moment");
        TextEntryController c = TextEntryController.enterText(
                Configuration.langBundle.getString("new_moment_name"),
                newMoment.getName(),
                50,
                new SuggestionStrategyMoment()
        );
        if(c.getState() == DialogState.SUCCESS){
            newMoment.setName(c.getValue());
        }

        //Model command creation
        AddSubMoment cmd;
        if(index == -1)
            cmd = new AddSubMoment(parent, newMoment);
        else
            cmd = new AddSubMoment(parent, newMoment, index);
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> { hooksNotifier.notifyMomentAdded(newMoment); });
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> { hooksNotifier.notifyMomentRemoved(newMoment); });
        HistoryManager.addCommand(cmd, true);

        if (concreteCategory != null) {
            new AddConcreteCategoryCommand(newMoment, concreteCategory, false).execute();
        }
        else if(schemaCategory != null) {
            new AddConcreteCategoryCommand(hooksNotifier, newMoment, schemaCategory, false).execute();
        }

        return null;
    }
}

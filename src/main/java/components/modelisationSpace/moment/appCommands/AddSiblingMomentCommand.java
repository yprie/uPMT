package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import application.history.ModelUserActionCommandHooks;
import components.modelisationSpace.category.appCommands.AddConcreteCategoryCommand;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.justification.appCommands.AddDescriptemeCommand;
import models.*;
import components.modelisationSpace.moment.modelCommands.AddSubMoment;
import utils.command.Executable;

public class AddSiblingMomentCommand implements Executable<Void> {

    private Descripteme descripteme = null;
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

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment, Descripteme descripteme) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        this.descripteme = descripteme;
    }

    public AddSiblingMomentCommand(ModelisationSpaceHookNotifier hooksNotifier, RootMoment parent, Moment newMoment, Descripteme descripteme, int index) {
        this.hooksNotifier = hooksNotifier;
        this.parent = parent;
        this.newMoment = newMoment;
        this.descripteme = descripteme;
        this.index = index;
    }

    @Override
    public Void execute() {
        //Model command creation
        AddSubMoment cmd;
        if(index == -1)
            cmd = new AddSubMoment(parent, newMoment);
        else
            cmd = new AddSubMoment(parent, newMoment, index);
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterExecute, () -> hooksNotifier.notifyMomentAdded(newMoment));
        cmd.hooks().setHook(ModelUserActionCommandHooks.HookMoment.AfterUndo, () -> hooksNotifier.notifyMomentRemoved(newMoment));
        HistoryManager.addCommand(cmd, true);

        if (concreteCategory != null) {
            new AddConcreteCategoryCommand(hooksNotifier, newMoment, concreteCategory, false).execute();
        }
        else if(schemaCategory != null) {
            new AddConcreteCategoryCommand(hooksNotifier, newMoment, schemaCategory, false).execute();
        }
        else if (descripteme != null) {
            new AddDescriptemeCommand(newMoment.getJustification(), descripteme, false).execute();
        }

        newMoment.addParent(parent);

        newMoment.getController().passInRenamingMode(true);

        return null;
    }
}

package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import components.modelisationSpace.category.appCommands.AddConcreteCategoryCommand;
import models.ConcreteCategory;
import models.Moment;
import models.RootMoment;
import components.modelisationSpace.moment.modelCommands.AddSubMoment;
import models.SchemaCategory;
import utils.command.Executable;

public class AddSiblingMomentCommand implements Executable<Void> {

    private RootMoment parent;
    private Moment newMoment;
    private ConcreteCategory concreteCategory;
    int index = -1;

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment, ConcreteCategory concreteCategory) {
        this.parent = parent;
        this.newMoment = newMoment;
        this.concreteCategory = concreteCategory;
    }

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment, ConcreteCategory concreteCategory, int index) {
        this.parent = parent;
        this.newMoment = newMoment;
        this.index = index;
        this.concreteCategory = concreteCategory;
    }

    public AddSiblingMomentCommand(RootMoment root, Moment newMoment, SchemaCategory schemaCategory, Moment parent) {
        this.parent = root;
        this.newMoment = newMoment;
        this.concreteCategory = new ConcreteCategory(schemaCategory);
    }

    public AddSiblingMomentCommand(RootMoment root, Moment newMoment, SchemaCategory schemaCategory, Moment parent, int index) {
        this.parent = root;
        this.newMoment = newMoment;
        this.concreteCategory = new ConcreteCategory(schemaCategory);
        this.index = index;
    }

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment, int index) {
        this.parent = parent;
        this.newMoment = newMoment;
        this.index = index;
        concreteCategory = null;
    }

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment) {
        this.parent = parent;
        this.newMoment = newMoment;
        concreteCategory = null;
    }

    @Override
    public Void execute() {
        AddSubMoment cmd;
        if(index == -1)
            cmd = new AddSubMoment(parent, newMoment);
        else
            cmd = new AddSubMoment(parent, newMoment, index);

        HistoryManager.addCommand(cmd, true);

        if (concreteCategory != null) {
            AddConcreteCategoryCommand categoryCmd = new AddConcreteCategoryCommand(newMoment, concreteCategory, false);
            categoryCmd.execute();
        }

        new RenameMomentCommand(newMoment, false).execute();
        return null;
    }
}

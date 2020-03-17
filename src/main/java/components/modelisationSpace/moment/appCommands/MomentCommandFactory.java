package components.modelisationSpace.moment.appCommands;

import models.ConcreteCategory;
import models.Moment;
import models.RootMoment;
import models.SchemaCategory;

public class MomentCommandFactory {

    RootMoment parent;

    public MomentCommandFactory(RootMoment parent) {
        this.parent = parent;
    }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, int index) {
        return new AddSiblingMomentCommand(parent, m, index);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m) {
        return new AddSiblingMomentCommand(parent, m);
    }


    public AddSiblingMomentCommand addSiblingCommand(Moment m, ConcreteCategory category) {
        return new AddSiblingMomentCommand(parent, m, category);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m, ConcreteCategory category, int index) {
        return new AddSiblingMomentCommand(parent, m, category, index);
    }

    public AddSiblingMomentCommand addSiblingCommand(Moment m, SchemaCategory category, Moment parent) {
        return new AddSiblingMomentCommand(this.parent, m, category, parent);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m, SchemaCategory category, Moment parent, int index) {
        return new AddSiblingMomentCommand(this.parent, m, category, parent, index);
    }

    public DeleteMomentCommand deleteCommand(Moment m) { return new DeleteMomentCommand(parent, m); }
    public RenameMomentCommand renameCommand(Moment m) { return new RenameMomentCommand(m); }
}

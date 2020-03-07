package components.modelisationSpace.moment.appCommands;

import models.ConcreteCategory;
import models.Moment;
import models.RootMoment;

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
        System.out.println("addSiblingCommand category");
        return new AddSiblingMomentCommand(parent, m, category);
    }
    public AddSiblingMomentCommand addSiblingCommand(Moment m, ConcreteCategory category, int index) {
        System.out.println("addSiblingCommand category index");
        return new AddSiblingMomentCommand(parent, m, category, index);
    }
    public DeleteMomentCommand deleteCommand(Moment m) { return new DeleteMomentCommand(parent, m); }
    public RenameMomentCommand renameCommand(Moment m) { return new RenameMomentCommand(m); }
}

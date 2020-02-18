package components.modelisationSpace.moment.appCommands;

import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;

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

    public DeleteMomentCommand deleteCommand(Moment m) { return new DeleteMomentCommand(parent, m); }
}

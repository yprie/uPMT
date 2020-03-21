package components.modelisationSpace.moment.appCommands;

import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.RenameMoment;

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
    public MoveMomentCommand moveMomentCommand(Moment m, int index){
        return new MoveMomentCommand(parent, m, index);
    }
    public MoveMomentCommand moveMomentCommand(Moment m){
        return new MoveMomentCommand(parent, m);
    }
    public DeleteMomentCommand deleteCommand(Moment m) { return new DeleteMomentCommand(parent, m); }
    public DeleteMoveMomentCommand deleteMoveCommand(Moment m) { return new DeleteMoveMomentCommand(parent, m); }
    public RenameMomentCommand renameCommand(Moment m) { return new RenameMomentCommand(m); }
}

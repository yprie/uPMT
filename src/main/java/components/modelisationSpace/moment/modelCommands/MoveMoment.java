package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;

public class MoveMoment extends ModelUserActionCommand {
    private RootMoment parent;
    private Moment moment;
    private int addIndex = -1;

    public MoveMoment(RootMoment parent, Moment moment) {
        this.parent = parent;
        this.moment = moment;
    }
    public MoveMoment(RootMoment parent, Moment moment, int index) {
        this.parent = parent;
        this.moment = moment;
        this.addIndex = index;
    }

    @Override
    public Void execute() {
        Moment clone = moment.clone();
        if(addIndex == -1) {
            parent.addMoment(clone);
        }
        else {
            parent.addMoment(addIndex, clone);
        }
        return null;
    }

    @Override
    public Void undo() {
        parent.removeMoment(moment);
        return null;
    }
}

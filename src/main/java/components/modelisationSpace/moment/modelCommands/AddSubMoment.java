package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;

public class AddSubMoment extends ModelUserActionCommand<Void, Void> {

    private RootMoment parent;
    private Moment moment;
    private int addIndex = -1;


    public AddSubMoment(RootMoment parent, Moment moment) {
        this.parent = parent;
        this.moment = moment;
    }

    public AddSubMoment(RootMoment parent, Moment moment, int index) {
        this.parent = parent;
        this.moment = moment;
        this.addIndex = index;
    }

    @Override
    public Void execute() {
        if(addIndex == -1) {
            parent.addMoment(moment);
        }
        else {
            parent.addMoment(addIndex, moment);
        }
        return null;
    }

    @Override
    public Void undo() {
        parent.removeMoment(moment);
        return null;
    }

}

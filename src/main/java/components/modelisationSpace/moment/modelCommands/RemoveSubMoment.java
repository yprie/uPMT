package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;

public class RemoveSubMoment extends ModelUserActionCommand {

    private RootMoment parent;
    private Moment moment;
    private int oldIndex ;


    public RemoveSubMoment(RootMoment parent, Moment moment) {
        this.parent = parent;
        this.moment = moment;
    }

    @Override
    public Void execute() {
        oldIndex = parent.indexOf(moment);
        parent.removeMoment(moment);
        return null;
    }

    @Override
    public Void undo() {
        parent.addMoment(oldIndex, moment);
        return null;
    }


}

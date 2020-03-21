package components.modelisationSpace.moment.modelCommands;

import application.history.HistoryManager;
import application.history.ModelUserActionCommand;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;

public class RemoveMoveMoment extends ModelUserActionCommand {

    private RootMoment parent;
    private Moment moment;
    private int oldIndex ;


    public RemoveMoveMoment(RootMoment parent, Moment moment) {
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
        HistoryManager.goBack();
        return null;
    }



}

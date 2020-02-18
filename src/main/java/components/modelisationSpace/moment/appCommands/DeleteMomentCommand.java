package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.RemoveSubMoment;
import utils.command.Executable;

public class DeleteMomentCommand implements Executable<Void> {

    RootMoment parent;
    Moment moment;

    public DeleteMomentCommand(RootMoment parent, Moment m) {
        this.parent = parent;
        this.moment = m;
    }

    @Override
    public Void execute() {
        RemoveSubMoment cmd = new RemoveSubMoment(parent, moment);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

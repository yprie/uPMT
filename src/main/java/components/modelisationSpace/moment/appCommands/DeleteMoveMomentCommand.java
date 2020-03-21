package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.RemoveMoveMoment;
import components.modelisationSpace.moment.modelCommands.RemoveSubMoment;
import utils.command.Executable;

public class DeleteMoveMomentCommand implements Executable<Void> {

    RootMoment parent;
    Moment moment;

    public DeleteMoveMomentCommand(RootMoment parent, Moment m) {
        this.parent = parent;
        this.moment = m;
    }

    @Override
    public Void execute() {
        RemoveMoveMoment cmd = new RemoveMoveMoment(parent, moment);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

package components.modelisationSpace.moment.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.AddSubMoment;
import components.modelisationSpace.moment.modelCommands.MoveMoment;
import utils.command.Executable;

public class MoveMomentCommand implements Executable<Void> {
    RootMoment parent;
    Moment moment;
    int index = -1;

    public MoveMomentCommand(RootMoment parent, Moment m, int index) {
        this.parent = parent;
        this.moment = m;
        this.index = index;
    }

    public MoveMomentCommand(RootMoment parent, Moment m) {
        this.parent = parent;
        this.moment = m;
    }

    @Override
    public Void execute() {
        MoveMoment cmd;
        if(index == -1)
            cmd = new MoveMoment(parent, moment);
        else
            cmd = new MoveMoment(parent, moment, index);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

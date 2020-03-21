package components.modelisationSpace.moment.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.MoveMoment;
import utils.command.Executable;

public class MoveMomentCommand implements Executable<Void> {
    RootMoment parent;
    RootMoment originParent;
    Moment moment;
    int index = -1;

    public MoveMomentCommand(RootMoment parent, RootMoment originParent, Moment m, int index) {
        this.parent = parent;
        this.originParent = originParent;
        this.moment = m;
        this.index = index;
    }

    public MoveMomentCommand(RootMoment parent, RootMoment originParent, Moment m) {
        this.parent = parent;
        this.originParent = originParent;
        this.moment = m;
    }

    @Override
    public Void execute() {
        MoveMoment cmd;
        if(index == -1)
            cmd = new MoveMoment(parent, originParent, moment);
        else
            cmd = new MoveMoment(parent, originParent,moment, index);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

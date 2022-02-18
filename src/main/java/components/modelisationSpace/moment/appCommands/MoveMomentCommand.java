package components.modelisationSpace.moment.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.moment.modelCommands.MoveMoment;
import models.Moment;
import models.RootMoment;
import utils.command.Executable;
import utils.popups.WarningPopup;

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

    private boolean submomentIsParent(Moment m) {
        if (m.equals(parent)) {
            return true;
        }
        else {
            if (m.numberOfSubMoments() > 0) {
                for (Moment submoment : m.momentsProperty()) {
                    if (submomentIsParent(submoment)) return true;
                }
            }
            return false;
        }
    }
    @Override
    public Void execute() {
        if (submomentIsParent(moment)) {
            WarningPopup.display(Configuration.langBundle.getString("move_under_submoment_warning"));
            return null;
        }


        MoveMoment cmd;
        if(index == -1)
            cmd = new MoveMoment(parent, originParent, moment);
        else
            cmd = new MoveMoment(parent, originParent,moment, index);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

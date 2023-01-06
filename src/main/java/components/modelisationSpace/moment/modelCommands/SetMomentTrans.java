package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;

public class SetMomentTrans extends ModelUserActionCommand {

    private Moment moment;
    private boolean oldstate;
    private boolean newstate;

    public SetMomentTrans(Moment m, boolean oldstate) {
        this.moment = m;
        this.oldstate = oldstate;
    }

    @Override
    public Object execute() {
        newstate = !oldstate;
        moment.setTransitional(newstate);
        moment.getController().displayTransitional();
        return null;
    }

    @Override
    public Object undo() {
        moment.setTransitional(oldstate);
        moment.getController().displayTransitional();
        return null;
    }

}
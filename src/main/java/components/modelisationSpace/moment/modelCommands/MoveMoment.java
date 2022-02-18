package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;
import models.RootMoment;


public class MoveMoment extends ModelUserActionCommand {
    private RootMoment parent;
    private RootMoment originParent;
    private Moment moment;
    private int addIndex = -1;
    private int originIndex;

    public MoveMoment(RootMoment parent, RootMoment originParent, Moment moment) {
        this.parent = parent;
        this.originParent = originParent;
        this.moment = moment;
    }
    public MoveMoment(RootMoment parent, RootMoment originParent, Moment moment, int index) {
        this.parent = parent;
        this.originParent = originParent;
        this.moment = moment;
        this.addIndex = index;
    }

    @Override
    public Void execute() {
        originIndex = originParent.indexOf(moment);
        originParent.removeMoment(moment);

        if(addIndex == -1) {
            parent.addMoment(moment);
        }
        else {
            if (originParent.equals(parent) && originIndex < addIndex) {
                addIndex -= 1;
            }
            parent.addMoment(addIndex, moment);
        }
        return null;
    }

    @Override
    public Void undo() {
        parent.removeMoment(moment);
        originParent.addMoment(originIndex, moment);
       return null;
    }
}

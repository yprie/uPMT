package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;

public class RenameMoment extends ModelUserActionCommand {

    private Moment moment;
    private String newName;
    private String oldName;

    public RenameMoment(Moment m, String newName) {
        this.moment = m;
        this.newName = newName;
    }

    @Override
    public Object execute() {
        oldName = moment.getName();
        moment.setName(newName);
        return null;
    }

    @Override
    public Object undo() {
        moment.setName(oldName);
        return null;
    }

}

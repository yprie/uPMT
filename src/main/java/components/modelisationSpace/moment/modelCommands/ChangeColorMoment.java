package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;

public class ChangeColorMoment extends ModelUserActionCommand {

    private Moment moment;
    private String oldColor;
    private String newColor;

    public ChangeColorMoment(Moment m, String newColor, String oldColor) {
        this.moment = m;
        this.newColor = newColor;
        this.oldColor = oldColor;
    }

    @Override
    public Object execute() {
        moment.setColor(newColor);
        return null;
    }

    @Override
    public Object undo() {
        moment.setColor(oldColor);
        return null;
    }

}

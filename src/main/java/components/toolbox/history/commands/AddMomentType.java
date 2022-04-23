package components.toolbox.history.commands;

import application.history.ModelUserActionCommand;
import components.toolbox.controllers.ToolBoxControllers;
import models.Moment;

public class AddMomentType extends ModelUserActionCommand {
    ToolBoxControllers toolBoxControllers;
    Moment moment;

    public AddMomentType(ToolBoxControllers tbc, Moment m) {
        this.toolBoxControllers = tbc;
        this.moment = m;
    }

    @Override
    public Object execute() {
        this.toolBoxControllers.addAMomentType(this.moment);
        return null;
    }

    @Override
    public Object undo() {
        this.toolBoxControllers.removeAMomentType(this.moment);
        return null;
    }
}

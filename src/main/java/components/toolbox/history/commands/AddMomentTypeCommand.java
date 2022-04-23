package components.toolbox.history.commands;

import application.history.HistoryManager;
import components.toolbox.controllers.ToolBoxControllers;
import models.Moment;
import utils.command.Executable;

public class AddMomentTypeCommand implements Executable {
    ToolBoxControllers toolBoxControllers;
    Moment moment;

    public AddMomentTypeCommand(ToolBoxControllers tbc, Moment m) {
        this.toolBoxControllers = tbc;
        this.moment = m;
    }

    @Override
    public Object execute() {
        HistoryManager.addCommand(new AddMomentType(this.toolBoxControllers, this.moment), true);
        return null;
    }
}

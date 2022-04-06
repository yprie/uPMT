package components.toolbox.initalizable.commands;

import application.history.HistoryManager;
import components.toolbox.initalizable.ToolBoxControllers;
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

package components.toolbox.history.commands;

import application.history.HistoryManager;
import components.toolbox.controllers.ToolBoxControllers;
import models.SchemaMomentType;
import utils.command.Executable;

public class AddSchemaMomentTypeCommand implements Executable {
    ToolBoxControllers toolBoxControllers;
    SchemaMomentType schemaMomentType;

    public AddSchemaMomentTypeCommand(ToolBoxControllers tbc, SchemaMomentType smt) {
        this.toolBoxControllers = tbc;
        this.schemaMomentType = smt;
    }

    @Override
    public Object execute() {
        HistoryManager.addCommand(new AddSchemaMomentType(this.toolBoxControllers, this.schemaMomentType), true);
        return null;
    }
}

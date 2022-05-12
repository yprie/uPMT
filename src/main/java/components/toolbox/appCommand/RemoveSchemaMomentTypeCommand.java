package components.toolbox.appCommand;

import application.history.HistoryManager;
import components.toolbox.controllers.ToolBoxControllers;
import components.toolbox.history.commands.RemoveSchemaMomentType;
import models.SchemaMomentType;
import utils.command.Executable;

public class RemoveSchemaMomentTypeCommand implements Executable {
    ToolBoxControllers toolBoxControllers;
    SchemaMomentType schemaMomentType;

    public RemoveSchemaMomentTypeCommand(ToolBoxControllers tbc, SchemaMomentType smt) {
        this.toolBoxControllers = tbc;
        this.schemaMomentType = smt;
    }

    @Override
    public Object execute() {
        HistoryManager.addCommand(new RemoveSchemaMomentType(this.toolBoxControllers, this.schemaMomentType), true);
        return null;
    }
}
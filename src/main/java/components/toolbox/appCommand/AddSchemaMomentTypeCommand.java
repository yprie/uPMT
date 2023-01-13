package components.toolbox.appCommand;

import application.history.HistoryManager;
import components.toolbox.controllers.ToolBoxControllers;
import components.toolbox.history.commands.AddSchemaMomentType;
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

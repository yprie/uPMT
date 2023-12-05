package components.toolbox.history.commands;

import application.history.ModelUserActionCommand;
import components.toolbox.controllers.ToolBoxControllers;
import models.SchemaMomentType;

public class RemoveSchemaMomentType extends ModelUserActionCommand {
    ToolBoxControllers toolBoxControllers;
    SchemaMomentType schemaMomentType;

    public RemoveSchemaMomentType(ToolBoxControllers tbc, SchemaMomentType smt) {
        this.toolBoxControllers = tbc;
        this.schemaMomentType = smt;
    }

    @Override
    public Object execute() {
        this.toolBoxControllers.removeAMomentType(this.schemaMomentType);
        return null;
    }

    @Override
    public Object undo() {
        this.toolBoxControllers.addAMomentType(this.schemaMomentType);
        return null;
    }
}

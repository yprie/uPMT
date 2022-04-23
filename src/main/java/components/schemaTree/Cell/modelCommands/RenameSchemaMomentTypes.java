package components.schemaTree.Cell.modelCommands;

import application.history.ModelUserActionCommand;
import components.toolbox.models.SchemaMomentType;

public class RenameSchemaMomentTypes extends ModelUserActionCommand {
    SchemaMomentType element;
    String newName;
    String originalName;

    public RenameSchemaMomentTypes(SchemaMomentType element, String newName) {
        this.element = element;
        this.newName = newName;
        this.originalName = element.getName();
    }

    @Override
    public Void execute() {
        this.element.getMomentTypeController().rename(newName);
        return null;
    }

    @Override
    public Void undo() {
        this.element.getMomentTypeController().rename(originalName);
        return null;
    }
}

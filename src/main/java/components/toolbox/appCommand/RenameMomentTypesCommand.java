package components.toolbox.appCommand;

import application.history.HistoryManager;
import components.schemaTree.Cell.modelCommands.RenameSchemaMomentTypes;
import models.SchemaMomentType;
import utils.command.Executable;

public class RenameMomentTypesCommand implements Executable {

    private SchemaMomentType element;
    private String newName;

    public RenameMomentTypesCommand(SchemaMomentType element, String newName) {
        this.element = element;
        this.newName = newName;
    }

    @Override
    public Object execute() {
        RenameSchemaMomentTypes rsmt = new RenameSchemaMomentTypes(element, newName);
        HistoryManager.addCommand(rsmt, true);

        return null;
    }
}

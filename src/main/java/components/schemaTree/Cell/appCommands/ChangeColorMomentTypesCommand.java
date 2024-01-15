package components.schemaTree.Cell.appCommands;

import application.history.HistoryManager;
import components.schemaTree.Cell.modelCommands.ChangeColorSchemaMomentType;
import components.schemaTree.Cell.modelCommands.ChangeColorTreePluggable;
import models.SchemaCategory;
import models.SchemaMomentType;
import utils.command.Executable;

public class ChangeColorMomentTypesCommand implements Executable {

    private SchemaMomentType momentType;
    private String newColor;
    private boolean userCommand;

    public ChangeColorMomentTypesCommand(SchemaMomentType mt, String newColor) {
        this.momentType = mt;
        this.newColor = newColor;
        this.userCommand = true;
    }

    @Override
    public Object execute() {
        String oldColor = momentType.getColor();
        HistoryManager.addCommand(new ChangeColorSchemaMomentType(momentType, newColor, oldColor), userCommand);
        return null;
    }
}

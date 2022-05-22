package components.schemaTree.Cell.appCommands;

import application.history.HistoryManager;
import components.schemaTree.Cell.modelCommands.ChangeColorTreePluggable;
import models.SchemaCategory;
import utils.command.Executable;

public class ChangeColorCategoryCommand implements Executable {

    private SchemaCategory category;
    private String newColor;
    private boolean userCommand;

    public ChangeColorCategoryCommand(SchemaCategory c, String newColor) {
        this.category = c;
        this.newColor = newColor;
        this.userCommand = true;
    }

    @Override
    public Object execute() {
        String oldColor = category.getColor();
        HistoryManager.addCommand(new ChangeColorTreePluggable(category, newColor, oldColor), userCommand);
        return null;
    }
}
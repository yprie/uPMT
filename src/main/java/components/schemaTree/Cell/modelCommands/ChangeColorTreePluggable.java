package components.schemaTree.Cell.modelCommands;


import application.history.ModelUserActionCommand;
import models.SchemaCategory;

public class ChangeColorTreePluggable extends ModelUserActionCommand {

    private SchemaCategory category;
    private String oldColor;
    private String newColor;

    public ChangeColorTreePluggable(SchemaCategory category, String newColor, String oldColor) {
        this.category = category;
        this.newColor = newColor;
        this.oldColor = oldColor;
    }

    @Override
    public Object execute() {
        category.setColor(newColor);
        return null;
    }

    @Override
    public Object undo() {
        category.setColor(oldColor);
        return null;
    }

}


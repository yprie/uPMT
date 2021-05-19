package components.schemaTree.Cell.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.moment.modelCommands.ChangeColorMoment;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import components.schemaTree.Cell.modelCommands.ChangeColorTreePluggable;
import models.Moment;
import models.SchemaCategory;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.command.Executable;
import utils.popups.TextEntryController;
import utils.popups.WarningPopup;

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
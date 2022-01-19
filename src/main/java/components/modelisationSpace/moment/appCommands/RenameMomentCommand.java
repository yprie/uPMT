package components.modelisationSpace.moment.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import models.Moment;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.command.Executable;
import utils.popups.TextEntryController;

// class normalement inutile
public class RenameMomentCommand implements Executable {

    private Moment moment;
    private boolean userCommand;

    public RenameMomentCommand(Moment m) {
        this.moment = m;
        this.userCommand = true;
    }

    public RenameMomentCommand(Moment m, boolean userCommand) {
        this.moment = m;
        this.userCommand = userCommand;
    }

    @Override
    public Object execute() {
        TextEntryController c = TextEntryController.enterText(
                Configuration.langBundle.getString("new_moment_name"),
                moment.getName(),
                50,
                new SuggestionStrategyMoment()
        );
        //c.setStrategy(new SuggestionStrategyMoment());
        if(c.getState() == DialogState.SUCCESS){
            HistoryManager.addCommand(new RenameMoment(moment, c.getValue()), userCommand);
        }
        return null;
    }
}

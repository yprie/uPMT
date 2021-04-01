package components.modelisationSpace.moment.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import models.Moment;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.command.Executable;
import utils.popups.TextEntryController;

public class SetMomentTransCommand implements Executable {


    private Moment moment;
    private boolean userCommand;

    public SetMomentTransCommand(Moment m) {
        this.moment = m;
        this.userCommand = true;
    }

    public SetMomentTransCommand(Moment m, boolean userCommand) {
        this.moment = m;
        this.userCommand = userCommand;
    }

    @Override
    public Object execute() {
        if (moment.getTransitional()) {
            moment.setNotTransitional();
        }
        else {
            moment.setTransitional();
        }
        return null;
    }
}

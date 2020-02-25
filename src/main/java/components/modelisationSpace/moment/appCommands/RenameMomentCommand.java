package components.modelisationSpace.moment.appCommands;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import utils.DialogState;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.command.Executable;
import utils.popups.TextEntryController;

public class RenameMomentCommand implements Executable {

    private Moment moment;

    public RenameMomentCommand(Moment m) {
        this.moment = m;
    }

    @Override
    public Object execute() {
        TextEntryController c = TextEntryController.enterText(
                Configuration.langBundle.getString("new_moment_name"),
                moment.getName(),
                20,
                new SuggestionStrategyMoment()
        );
        //c.setStrategy(new SuggestionStrategyMoment());
        if(c.getState() == DialogState.SUCCESS){
            HistoryManager.addCommand(new RenameMoment(moment, c.getValue()), true);
        }
        return null;
    }
}

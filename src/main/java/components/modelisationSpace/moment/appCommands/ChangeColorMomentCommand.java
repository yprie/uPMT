package components.modelisationSpace.moment.appCommands;


import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.moment.modelCommands.ChangeColorMoment;
import models.Moment;
import utils.command.Executable;
import utils.popups.WarningPopup;

public class ChangeColorMomentCommand implements Executable {

    private Moment moment;
    private String newColor;
    private boolean userCommand;

    public ChangeColorMomentCommand(Moment m, String newColor) {
        this.moment = m;
        this.newColor = newColor;
        this.userCommand = true;
    }

    @Override
    public Object execute() {
        String oldColor = moment.getColor();
        if (this.moment.getTransitional()) {
            WarningPopup.display(Configuration.langBundle.getString("color_of_transitional"));
        }
        else {
            HistoryManager.addCommand(new ChangeColorMoment(moment, newColor, oldColor), userCommand);
        }
        return null;
    }
}
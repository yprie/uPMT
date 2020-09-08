package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import models.Descripteme;
import components.modelisationSpace.justification.modelCommands.AddDescripteme;
import models.Justification;
import utils.command.Executable;

public class AddDescriptemeCommand implements Executable<Void> {

    private Justification justification;
    private Descripteme descripteme;
    private int index;
    private boolean newUserAction = true;

    public AddDescriptemeCommand(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
        this.index = -1;
    }

    public AddDescriptemeCommand(Justification j, Descripteme d, boolean newUserAction) {
        this.justification = j;
        this.descripteme = d;
        this.index = -1;
        this.newUserAction = newUserAction;
    }

    public AddDescriptemeCommand(Justification j, Descripteme d, int index) {
        this.justification = j;
        this.descripteme = d;
        this.index = index;
    }

    @Override
    public Void execute() {
        if(index == -1){
            HistoryManager.addCommand(new AddDescripteme(justification, descripteme), newUserAction);
        }
        else {
            HistoryManager.addCommand(new AddDescripteme(justification, descripteme, index), newUserAction);
        }
        return null;
    }

}

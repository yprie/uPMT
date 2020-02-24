package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.modelCommands.RemoveDescripteme;
import components.modelisationSpace.justification.models.Justification;
import utils.command.Executable;

public class RemoveDescriptemeCommand implements Executable<Void> {

    private Justification justification;
    private Descripteme descripteme;
    private boolean newUserAction = true;

    public RemoveDescriptemeCommand(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
    }

    public void isNotUserAction() { newUserAction = false; }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveDescripteme(justification, descripteme), newUserAction);
        return null;
    }

}

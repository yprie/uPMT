package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.modelCommands.AddDescripteme;
import components.modelisationSpace.justification.models.Justification;
import utils.command.Executable;

public class DuplicateDescriptemeCommand implements Executable<Void> {

    private Justification justification;
    private Descripteme descripteme;

    public DuplicateDescriptemeCommand(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
    }

    @Override
    public Void execute() {
        Descripteme newDescripteme = descripteme.duplicate();
        int indexCurrent = justification.indexOf(descripteme);
        HistoryManager.addCommand(new AddDescripteme(justification, newDescripteme, indexCurrent+1), true);
        return null;
    }
}

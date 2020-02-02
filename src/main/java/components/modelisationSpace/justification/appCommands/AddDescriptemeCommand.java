package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.modelCommands.AddDescripteme;
import components.modelisationSpace.justification.models.Justification;
import utils.command.Executable;

public class AddDescriptemeCommand implements Executable<Void> {

    private Justification justification;
    private Descripteme descripteme;
    private int index;

    public AddDescriptemeCommand(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
        this.index = -1;
    }

    public AddDescriptemeCommand(Justification j, Descripteme d, int index) {
        this.justification = j;
        this.descripteme = d;
        this.index = index;
    }

    @Override
    public Void execute() {
        if(index == -1){
            HistoryManager.addCommand(new AddDescripteme(justification, descripteme), true);
        }
        else{
            HistoryManager.addCommand(new AddDescripteme(justification, descripteme, index), true);
        }
        return null;
    }

}

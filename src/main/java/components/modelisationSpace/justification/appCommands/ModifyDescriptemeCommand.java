package components.modelisationSpace.justification.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.justification.modelCommands.ModifyDescripteme;
import models.Descripteme;
import utils.command.Executable;

public class ModifyDescriptemeCommand implements Executable<Void> {

    private Descripteme descripteme;
    private int end;
    private int start;

    public ModifyDescriptemeCommand(Descripteme d, int start, int end) {
        this.descripteme = d;
        this.start = start;
        this.end = end;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new ModifyDescripteme(descripteme, start, end), true);
        return null;
    }
}

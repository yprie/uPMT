package components.interviewPanel.appCommands;

import models.Descripteme;
import utils.command.Executable;

public class RevealDescriptemeCommand implements Executable<Void> {
    Descripteme descripteme;

    public RevealDescriptemeCommand(Descripteme descripteme) {
        this.descripteme = descripteme;
    }

    @Override
    public Void execute() {
        descripteme.getEmphasizeProperty().set(!descripteme.getEmphasizeProperty().get());
        return null;
    }
}

package components.modelisationSpace.justification.modelCommands;

import application.history.ModelUserActionCommand;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.models.Justification;

public class RemoveDescripteme extends ModelUserActionCommand<Void, Void> {

    private Justification justification;
    private Descripteme descripteme;
    int index;

    public RemoveDescripteme(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
    }

    @Override
    public Void execute() {
        index = this.justification.indexOf(descripteme);
        this.justification.removeDescripteme(descripteme);
        return null;
    }

    @Override
    public Void undo() {
        this.justification.addDescripteme(descripteme, index);
        return null;
    }
}

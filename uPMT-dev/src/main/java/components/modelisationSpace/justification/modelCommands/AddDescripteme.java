package components.modelisationSpace.justification.modelCommands;

import application.history.ModelUserActionCommand;
import models.Descripteme;
import models.Justification;

public class AddDescripteme extends ModelUserActionCommand<Void, Void> {

    private Justification justification;
    private Descripteme descripteme;
    private int index = -1;

    public AddDescripteme(Justification j, Descripteme d) {
        this.justification = j;
        this.descripteme = d;
    }

    public AddDescripteme(Justification j, Descripteme d, int index) {
        this.justification = j;
        this.descripteme = d;
        this.index = index;
    }

    @Override
    public Void execute() {
        if(index == -1)
            this.justification.addDescripteme(descripteme);
        else
            this.justification.addDescripteme(descripteme, index);
        this.descripteme.getInterviewText().addDescripteme(descripteme);
        return null;
    }

    @Override
    public Void undo() {
        this.justification.removeDescripteme(descripteme);
        this.descripteme.getInterviewText().removeDescripteme(descripteme);
        return null;
    }
}

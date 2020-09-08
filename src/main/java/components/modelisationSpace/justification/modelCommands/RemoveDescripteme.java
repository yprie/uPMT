package components.modelisationSpace.justification.modelCommands;

import application.history.ModelUserActionCommand;
import models.Descripteme;
import models.Justification;
import utils.GlobalVariables;

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
        this.descripteme.getInterviewText().removeDescripteme(descripteme);
        GlobalVariables.getGlobalVariables().setDescriptemeChanged(descripteme);
        return null;
    }

    @Override
    public Void undo() {
        this.justification.addDescripteme(descripteme, index);
        this.descripteme.getInterviewText().addDescripteme(descripteme);
        GlobalVariables.getGlobalVariables().setDescriptemeChanged(descripteme);
        return null;
    }
}

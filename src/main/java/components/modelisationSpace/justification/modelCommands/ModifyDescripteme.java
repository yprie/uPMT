package components.modelisationSpace.justification.modelCommands;

import application.history.ModelUserActionCommand;
import models.Descripteme;

public class ModifyDescripteme extends ModelUserActionCommand<Void, Void> {
    private int start;
    private int oldStart;
    private int oldEnd;
    private int end;
    private Descripteme descripteme;

    public ModifyDescripteme(Descripteme d, int start, int end) {
        descripteme = d;
        oldStart = descripteme.getStartIndex();
        oldEnd = descripteme.getEndIndex();
        this.start = start;
        this.end = end;
    }

    @Override
    public Void execute() {
        descripteme.modifyIndex(start, end);
        return null;
    }

    @Override
    public Void undo() {
        descripteme.modifyIndex(oldStart, oldEnd);
        return null;
    }
}

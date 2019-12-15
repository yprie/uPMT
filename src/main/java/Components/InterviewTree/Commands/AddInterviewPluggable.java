package Components.InterviewTree.Commands;

import Components.InterviewTree.InterviewTreePluggable;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import application.History.ModelUserActionCommand;
import com.google.gson.internal.$Gson$Preconditions;

public class AddInterviewPluggable extends ModelUserActionCommand<Void, Void> {

    private InterviewTreePluggable parent;
    private InterviewTreePluggable element;
    private boolean shouldElementAlreadyBeRenamed;
    private boolean shouldElementBeRenamed;

    public AddInterviewPluggable(InterviewTreePluggable parent, InterviewTreePluggable element, boolean shouldElementBeRenamed) {
        this.parent = parent;
        this.element = element;
        this.shouldElementAlreadyBeRenamed = element.mustBeRenamed();
        this.shouldElementBeRenamed = shouldElementBeRenamed;
    }

    @Override
    public Void execute() {
        parent.addChild(element);
        if(shouldElementBeRenamed)
            element.setMustBeRenamed(true);
        return null;
    }

    @Override
    public Void undo() {
        element.setMustBeRenamed(shouldElementAlreadyBeRenamed);
        parent.removeChild(element);
        return null;
    }

}

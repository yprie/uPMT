package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;

public class AddComment extends ModelUserActionCommand {
    Moment moment;
    String newComment;
    String oldComment;
    public AddComment(Moment moment, String newComment){
        this.moment = moment;
        this.newComment = newComment;
    }
    @Override
    public Object undo() {
        moment.setComment(oldComment);
        return null;
    }

    @Override
    public Object execute() {
        this.oldComment = moment.getComment();
        moment.setComment(newComment);
        return null;
    }
}

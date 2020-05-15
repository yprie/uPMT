package components.modelisationSpace.moment.appCommands;

import application.history.HistoryManager;
import components.modelisationSpace.moment.modelCommands.AddComment;
import models.Moment;
import utils.command.Executable;

public class AddCommentCommand implements Executable {
    Moment moment;
    String comment;

    AddCommentCommand(Moment moment, String comment){
        this.moment = moment;
        this.comment = comment;
    }
    @Override
    public Object execute() {
        HistoryManager.addCommand(new AddComment(moment, comment), true);
        return null;
    }
}

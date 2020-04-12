package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import models.Interview;

import java.time.LocalDate;

public class EditInterviewCommand extends ModelUserActionCommand<Void, Void> {
    Interview interview;
    LocalDate oldDate;
    String oldParticipantName;
    String oldComment;
    LocalDate newDate;
    String newParticipantName;
    String newComment;

    public EditInterviewCommand(Interview interview, LocalDate newDate, String newParticipantName, String newComment){
        this.interview = interview;
        this.newDate = newDate;
        this.newParticipantName = newParticipantName;
        this.newComment = newComment;
    }

    @Override
    public Void undo() {
        interview.setDate(oldDate);
        interview.setParticipantName(oldParticipantName);
        interview.setComment(oldComment);
        return null;
    }

    @Override
    public Void execute() {
        oldDate = interview.getDate();
        oldParticipantName = interview.getParticipantName();
        oldComment = interview.getComment();
        interview.setDate(newDate);
        interview.setParticipantName(newParticipantName);
        interview.setComment(newComment);
        return null;
    }
}

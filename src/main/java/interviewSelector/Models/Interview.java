package interviewSelector.Models;

import Components.InterviewPanel.Models.InterviewText;

import java.io.Serializable;
import java.time.LocalDate;

public class Interview implements Serializable {

    private String participantName;
    private LocalDate date;
    private String comment;
    private InterviewText interviewText;

    public Interview(String participantName, LocalDate date, InterviewText interviewText) {
        this.participantName = participantName;
        this.date = date;
        this.interviewText = interviewText;
    }

    public String getParticipantName() { return this.participantName; }
    public LocalDate getDate() { return this.date; }
    public InterviewText getInterviewText() { return this.interviewText; }

    public String getComment() { return this.comment; }
    public void setComment(String comment) { this.comment = comment; }

    //display interview in trees
    public String getTitle(){
        return(this.participantName+" "+date.toString());
    }
}

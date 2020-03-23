package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Interview implements Serializable {

    private String participantName;
    private LocalDate date;
    private String comment;
    private InterviewText interviewText;
    private RootMoment rootMoment;

    public Interview(String participantName, LocalDate date, InterviewText interviewText, RootMoment rootMoment) {
        this.participantName = participantName;
        this.date = date;
        this.interviewText = interviewText;
        this.rootMoment = rootMoment;
    }

    public String getParticipantName() { return this.participantName; }
    public LocalDate getDate() { return this.date; }
    public InterviewText getInterviewText() { return this.interviewText; }

    public String getComment() { return this.comment; }
    public void setComment(String comment) { this.comment = comment; }

    //display interview in trees
    public String getTitle(){
        return getTitle(participantName, date);
    }

    public static String getTitle(String participantName, LocalDate date) {
        String dateStr;
        if (date == null) {
            dateStr = "";
        }
        else {
            dateStr = date.toString();
        }
        if(!dateStr.equals("")) {
            return participantName + "_" + dateStr;
        }
        else {
            return participantName;
        }
    }

    public RootMoment getRootMoment() { return rootMoment; }
}

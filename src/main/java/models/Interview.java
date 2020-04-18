package models;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class Interview implements Serializable {
    private SimpleStringProperty title;
    private SimpleStringProperty participantName;
    private LocalDate date;
    private SimpleStringProperty comment;
    private InterviewText interviewText;
    private RootMoment rootMoment;

    public Interview(String participantName, LocalDate date, InterviewText interviewText, RootMoment rootMoment) {
        this.participantName = new SimpleStringProperty(participantName);
        this.date = date;
        this.interviewText = interviewText;
        this.rootMoment = rootMoment;
        this.title = new SimpleStringProperty(getTitle(participantName, date));
        this.comment = new SimpleStringProperty();
    }

    public String getTitle(){
        return title.get();
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void updateTitle(){
        title.set(getTitle(getParticipantName(), date));
    }

    public String getParticipantName() { return participantName.get(); }

    public SimpleStringProperty participantNameProperty() {
        return participantName;
    }

    public void setParticipantName(String participantName) { this.participantName.set(participantName); }

    public LocalDate getDate() { return this.date; }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public InterviewText getInterviewText() { return this.interviewText; }


    public String getComment() { return this.comment.get(); }
    public void setComment(String comment) { this.comment.set(comment); }
    public SimpleStringProperty commentProperty() { return this.comment; }


//    //display interview in trees
//    public String gettitle(){
//        return getTitle(participantName, date);
//    }

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

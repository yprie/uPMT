package models;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

import java.io.Serializable;
import java.time.LocalDate;

public class Interview implements Serializable, IDraggable {
    private SimpleStringProperty title;
    private SimpleStringProperty participantName;
    private LocalDate date;
    private SimpleStringProperty comment;
    private InterviewText interviewText;
    private RootMoment rootMoment;


    private SimpleStringProperty color = new SimpleStringProperty("ffffff");
    public static final DataFormat format = new DataFormat("Interview");
    public Interview(String participantName, LocalDate date, InterviewText interviewText, RootMoment rootMoment) {
        this.participantName = new SimpleStringProperty(participantName);
        this.date = date;
        this.interviewText = interviewText;
        this.rootMoment = rootMoment;
        this.title = new SimpleStringProperty(getTitle(participantName, date));
        this.comment = new SimpleStringProperty();

    }
    public Interview(String participantName, LocalDate date, InterviewText interviewText, RootMoment rootMoment,String color) {
        this.participantName = new SimpleStringProperty(participantName);
        this.date = date;
        this.interviewText = interviewText;
        this.rootMoment = rootMoment;
        this.color.set(color);
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
//    public String getTitle(){
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
    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }
    public RootMoment getRootMoment() { return rootMoment; }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }
    public SimpleStringProperty colorProperty() {
        return color;
    }
}

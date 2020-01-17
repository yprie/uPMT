package persistency.newSaveSystem;

import components.interviewSelector.models.Interview;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.time.LocalDate;

public class SInterview extends Serializable<Interview> {

    //General info
    public static final int version = 1;
    public static final String modelName = "interview";

    //Fields
    public String participantName;
    public LocalDate date;
    public String comment;
    public SInterviewText interviewText;

    public SInterview(ObjectSerializer serializer) {
        super(serializer);
    }
    public SInterview(Interview modelReference) {
        super(modelName, version, modelReference);
        this.participantName = modelReference.getParticipantName();
        this.date = modelReference.getDate();
        this.comment = modelReference.getComment();
        this.interviewText = new SInterviewText(modelReference.getInterviewText());
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        participantName = serializer.getString("participantName");
        date = serializer.getLocalDate("date");
        comment = serializer.getFacultativeString("comment", null);
        interviewText = serializer.getObject(SInterviewText.modelName, SInterviewText::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeString("participantName", participantName);
        serializer.writeLocalDate("date", date);
        serializer.writeFacultativeString("comment", comment);
        serializer.writeObject(SInterviewText.modelName, interviewText);
    }

    @Override
    protected Interview createModel() {
        Interview i = new Interview(participantName, date, interviewText.convertToModel());
        if(this.comment != null)
            i.setComment(comment);
        return i;
    }


}

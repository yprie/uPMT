package persistency.newSaveSystem;

import models.Interview;
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
    public SRootMoment rootMoment;
    public  String color;

    public SInterview(ObjectSerializer serializer) {
        super(serializer);
    }

    public SInterview(ObjectSerializer serializer, Interview modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(Interview modelReference) {
        this.participantName = modelReference.getParticipantName();
        this.date = modelReference.getDate();
        this.comment = modelReference.getComment();
        this.interviewText = new SInterviewText(serializer, modelReference.getInterviewText());
        this.rootMoment = new SRootMoment(serializer, modelReference.getRootMoment());
        this.color=modelReference.getColor();
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        participantName = serializer.getString("participantName");
        date = serializer.getLocalDate("date");
        comment = serializer.getFacultativeString("comment", null);
        interviewText = serializer.getObject(SInterviewText.modelName, SInterviewText::new);
        rootMoment = serializer.getObject(SRootMoment.modelName, SRootMoment::new);
        color=serializer.getFacultativeString("color","ffffff");
    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeString("participantName", participantName);
        serializer.writeLocalDate("date", date);
        serializer.writeFacultativeString("comment", comment);
        serializer.writeObject(SInterviewText.modelName, interviewText);
        serializer.writeObject(SRootMoment.modelName, rootMoment);
        if(color==null){
            System.out.println("wtf");
        }

        serializer.writeString("color",color);
    }

    @Override
    protected Interview createModel() {
        Interview i = new Interview(participantName, date, interviewText.convertToModel(), rootMoment.convertToModel(),color);
        if(this.comment != null)
            i.setComment(comment);
        return i;
    }


}

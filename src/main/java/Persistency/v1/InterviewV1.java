package Persistency.v1;

import Persistency.PersistentElement;
import Persistency.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.time.LocalDate;

public class InterviewV1 implements PersistentElement {

    public String participantName;
    public LocalDate date;
    public String comment;
    public InterviewTextV1 interviewText;

    @Override
    public void write(ByteArrayOutputStream bytes, Serializer sr) {
        SerializerV1.writeString(bytes, participantName);
        SerializerV1.writeString(bytes, comment);
        SerializerV1.writeLocalDate(bytes, date);
        sr.writeObject(bytes, interviewText);
    }

    @Override
    public void read(ByteBuffer bytes, Serializer sr) {
        participantName = SerializerV1.readString(bytes);
        comment = SerializerV1.readString(bytes);
        date = SerializerV1.readLocalDate(bytes);
        interviewText = sr.readObject(bytes, new InterviewTextV1());
    }
}

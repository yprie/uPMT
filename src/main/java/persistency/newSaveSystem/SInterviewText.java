package persistency.newSaveSystem;

import models.InterviewText;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SInterviewText extends Serializable<InterviewText> {

    //General info
    public static final int version = 1;
    public static final String modelName = "interviewText";

    //Fields
    public String text;

    public SInterviewText(ObjectSerializer serializer) {
        super(serializer);
    }
    public SInterviewText(InterviewText objectReference) {
        super(modelName, version, objectReference);
        this.text = objectReference.getText();
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        text = serializer.getString("text");
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("text", text);
    }

    @Override
    protected InterviewText createModel() {
        return new InterviewText(text);
    }
}

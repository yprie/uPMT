package persistency.newSaveSystem;

import models.Descripteme;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SDescripteme extends Serializable<Descripteme> {

    public static final int version = 1;
    public static final String modelName = "descripteme";

    public int startIndex;
    public int endIndex;
    public SInterviewText interviewText;

    public SDescripteme(ObjectSerializer serializer) {
        super(serializer);
    }

    public SDescripteme(ObjectSerializer serializer, Descripteme modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(Descripteme modelReference) {
        this.startIndex = modelReference.getStartIndex();
        this.endIndex = modelReference.getEndIndex();
        this.interviewText = new SInterviewText(serializer, modelReference.getInterviewText());
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        startIndex = serializer.getInt("startIndex");
        endIndex = serializer.getInt("endIndex");
        interviewText = serializer.getObject("interviewText", SInterviewText::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeInt("startIndex", startIndex);
        serializer.writeInt("endIndex", endIndex);
        serializer.writeObject("interviewText", interviewText);
    }

    @Override
    protected Descripteme createModel() {
        return new Descripteme(interviewText.convertToModel(), startIndex, endIndex);
    }
}

package persistency.newSaveSystem;

import javafx.scene.paint.Color;
import models.Annotation;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SAnnotation extends Serializable<Annotation> {
    public static final int version = 1;
    public static final String modelName = "annotation";

    public int startIndex;
    public int endIndex;
    public Color color;
    public SInterviewText interviewText;

    public SAnnotation(ObjectSerializer serializer) {
        super(serializer);
    }

    public SAnnotation(Annotation modelReference, SInterviewText interviewText) {
        super(modelName, version, modelReference);

        this.startIndex = modelReference.getStartIndex();
        this.endIndex = modelReference.getEndIndex();
        this.color = modelReference.getColor();
        this.interviewText = interviewText;
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        startIndex = serializer.getInt("startIndex");
        endIndex = serializer.getInt("endIndex");
        color = serializer.getColor("color");
        interviewText = serializer.getObject("interviewText", SInterviewText::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeInt("startIndex", startIndex);
        serializer.writeInt("endIndex", endIndex);
        serializer.writeColor("color", color);
        serializer.writeObject("interviewText", interviewText);
    }

    @Override
    protected Annotation createModel() {
        return new Annotation(interviewText.convertToModel(), startIndex, endIndex, color);
    }
}

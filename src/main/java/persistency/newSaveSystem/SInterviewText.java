package persistency.newSaveSystem;

import models.Annotation;
import models.InterviewText;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SInterviewText extends Serializable<InterviewText> {

    //General info
    public static final int version = 1;
    public static final String modelName = "interviewText";


    //Fields
    public String text;
    public ArrayList<SAnnotation> annotations;

    public SInterviewText(ObjectSerializer serializer) {
        super(serializer);
    }
    public SInterviewText(InterviewText objectReference) {
        super(modelName, version, objectReference);
        this.text = objectReference.getText();
        annotations = new ArrayList<>();
        for (Annotation annotation : objectReference.getAnnotationsProperty())
            annotations.add(new SAnnotation(annotation, this));
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        text = serializer.getString("text");
        annotations = serializer.getArray(serializer.setListSuffix(SAnnotation.modelName), SAnnotation::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("text", text);
        serializer.writeArray(serializer.setListSuffix(SAnnotation.modelName), annotations);
    }

    @Override
    protected InterviewText createModel() {
        ArrayList<Annotation> annotations = new ArrayList<>();
        for (SAnnotation sAnnotation : this.annotations) {
            annotations.add(sAnnotation.convertToModel());
        }
        return new InterviewText(text, annotations);
    }
}

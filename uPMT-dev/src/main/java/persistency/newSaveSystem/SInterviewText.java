package persistency.newSaveSystem;

import models.Annotation;
import models.Interview;
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

    public SInterviewText(ObjectSerializer serializer, InterviewText objectReference) {
        super(serializer, modelName, version, objectReference);
    }

    @Override
    public void init(InterviewText modelReference) {
        this.text = modelReference.getText();
        annotations = new ArrayList<>();
        for (Annotation annotation : modelReference.getAnnotationsProperty())
            annotations.add(new SAnnotation(serializer, annotation));
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
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
        return new InterviewText(text);
    }

    @Override
    protected void finalizeModelCreation(InterviewText model) {
        for (SAnnotation sAnnotation : this.annotations) {
            model.addAnnotation(sAnnotation.convertToModel());
        }
    }
}

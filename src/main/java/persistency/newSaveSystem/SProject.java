package persistency.newSaveSystem;

import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;


import java.util.ArrayList;

public class SProject extends Serializable {

    public static final int version = 1;
    public static final String modelName = "project";

    public String name;
    public SSchemaTreeRoot schemaTreeRoot;
    public ArrayList<SInterview> interviews;

    public SProject(ObjectSerializer serializer) {
        super(serializer);
    }

    public SProject(Object modelReference) {
        super(modelName, version, modelReference);
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        name = serializer.getString("name");
        schemaTreeRoot = new SSchemaTreeRoot(serializer.getObject(modelName));

        interviews = new ArrayList<>();
        ArrayList<ObjectSerializer> interview_serializers = serializer.getArray(SInterview.modelName);
        for (ObjectSerializer interview_serializer : interview_serializers)
            interviews.add(new SInterview(interview_serializer));
    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeObject(SSchemaTreeRoot.modelName, schemaTreeRoot);
        serializer.writeArray(SInterview.modelName + "_list", interviews);
    }

}

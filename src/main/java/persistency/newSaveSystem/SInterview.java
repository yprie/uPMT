package persistency.newSaveSystem;

import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SInterview extends Serializable {

    public static final int version = 1;
    public static final String modelName = "interview";

    public SInterview(ObjectSerializer serializer) {
        super(serializer);
    }

    public SInterview(Object modelReference) {
        super(modelName, version, modelReference);
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {

    }

    @Override
    public void write(ObjectSerializer serializer) {
        serializer.writeInt("randomValue", 15);
    }


}

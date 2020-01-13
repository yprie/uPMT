package persistency.newSaveSystem;

import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;


public class SSchemaTreeRoot extends Serializable {

    public static final int version = 1;
    public static final String modelName = "schemaTreeRoot";

    //public PersistentListV1<SchemaFolderV1> folders;

    public SSchemaTreeRoot(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaTreeRoot(Object modelReference) {
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

    }

}


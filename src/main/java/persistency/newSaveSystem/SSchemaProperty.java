package persistency.newSaveSystem;

import models.SchemaProperty;
import persistency.newSaveSystem.serialization.ObjectSerializer;

public class SSchemaProperty extends SSchemaElement<SchemaProperty> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaProperty";

    //Fields

    public SSchemaProperty(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaProperty(ObjectSerializer serializer, SchemaProperty modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(SchemaProperty modelReference) {
        super.init(modelReference);
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected SchemaProperty createModel() {
        SchemaProperty property = new SchemaProperty(name);
        property.expandedProperty().set(expanded);

        return property;
    }


}

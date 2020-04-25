package persistency.newSaveSystem;

import models.SchemaElement;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public abstract class SSchemaElement<ModelType extends SchemaElement> extends Serializable<ModelType> {

    //Fields
    public String name;
    public boolean expanded;

    public SSchemaElement(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaElement(ObjectSerializer serializer, String modelName, int version, ModelType modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(ModelType modelReference) {
        this.name = modelReference.getName();
        this.expanded = modelReference.isExpanded();
    }

    @Override
    protected void read() {
        name = serializer.getString("name");
        expanded = serializer.getBoolean("expanded");
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeBoolean("expanded", expanded);
    }
}

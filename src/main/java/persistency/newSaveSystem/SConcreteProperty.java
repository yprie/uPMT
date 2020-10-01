package persistency.newSaveSystem;

import models.ConcreteProperty;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SConcreteProperty extends Serializable<ConcreteProperty> {

    //General info
    public static final int version = 1;
    public static final String modelName = "concreteProperty";

    public SSchemaProperty schemaProperty;
    public SJustification justification;
    public String value;

    public SConcreteProperty(ObjectSerializer serializer) {
        super(serializer);
    }

    public SConcreteProperty(ObjectSerializer serializer, ConcreteProperty modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(ConcreteProperty modelReference) {
        schemaProperty = new SSchemaProperty(serializer, modelReference.getSchemaProperty());
        justification = new SJustification(serializer, modelReference.getJustification());
        value = modelReference.getValue();
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        schemaProperty = serializer.getObject("schemaProperty", SSchemaProperty::new);
        justification = serializer.getObject("justification", SJustification::new);
        value = serializer.getString("value");
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeObject("schemaProperty", schemaProperty);
        serializer.writeObject("justification", justification);
        serializer.writeString("value", value);
    }

    @Override
    protected ConcreteProperty createModel() {
        ConcreteProperty cp = new ConcreteProperty(schemaProperty.convertToModel(), justification.convertToModel());
        cp.setValue(value);
        return cp;
    }
}

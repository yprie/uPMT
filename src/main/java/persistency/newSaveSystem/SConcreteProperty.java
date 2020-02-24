package persistency.newSaveSystem;

import components.modelisationSpace.property.model.ConcreteProperty;
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

    public SConcreteProperty(ConcreteProperty modelReference) {
        super(modelName, version, modelReference);

        schemaProperty = new SSchemaProperty(modelReference.getSchemaProperty());
        justification = new SJustification(modelReference.getJustification());
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

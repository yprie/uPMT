package persistency.newSaveSystem;

import models.ConcreteCategory;
import models.ConcreteProperty;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SConcreteCategory extends Serializable<ConcreteCategory> {

    //General info
    public static final int version = 1;
    public static final String modelName = "concreteCategory";

    public SSchemaCategory schemaCategory;
    public SJustification justification;
    public ArrayList<SConcreteProperty> properties;

    public SConcreteCategory(ObjectSerializer serializer) {
        super(serializer);
    }

    public SConcreteCategory(ObjectSerializer serializer, ConcreteCategory modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(ConcreteCategory modelReference) {
        schemaCategory = new SSchemaCategory(serializer, modelReference.getSchemaCategory());
        justification = new SJustification(serializer, modelReference.getJustification());
        properties = new ArrayList<>();
        for(ConcreteProperty p: modelReference.propertiesProperty())
            properties.add(new SConcreteProperty(serializer, p));
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        schemaCategory = serializer.getObject("schemaCategory", SSchemaCategory::new);
        justification = serializer.getObject("justification", SJustification::new);
        properties = serializer.getArray(serializer.setListSuffix(SConcreteProperty.modelName), SConcreteProperty::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeObject("schemaCategory", schemaCategory);
        serializer.writeObject("justification", justification);
        serializer.writeArray(serializer.setListSuffix(SConcreteProperty.modelName), properties);
    }

    @Override
    protected ConcreteCategory createModel() {
        ArrayList<ConcreteProperty> props = new ArrayList<>();
        for(SConcreteProperty cp: properties)
            props.add(cp.convertToModel());

        return new ConcreteCategory(schemaCategory.convertToModel(), justification.convertToModel(), props);
    }
}

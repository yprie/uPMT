package persistency.newSaveSystem;

import models.SchemaCategory;
import models.SchemaProperty;
import persistency.newSaveSystem.serialization.ObjectSerializer;

import java.util.ArrayList;

public class SSchemaCategory extends SSchemaElement<SchemaCategory> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaCategory";

    //Fields
    public ArrayList<SSchemaProperty> properties;

    public SSchemaCategory(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaCategory(ObjectSerializer serializer, SchemaCategory modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(SchemaCategory modelReference) {
        super.init(modelReference);
        this.properties = new ArrayList<>();
        for(SchemaProperty p: modelReference.propertiesProperty()) {
            properties.add(new SSchemaProperty(serializer, p));
        }
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        super.read();
        properties = serializer.getArray(serializer.setListSuffix(SSchemaProperty.modelName), SSchemaProperty::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        super.write(serializer);
        serializer.writeArray(serializer.setListSuffix(SSchemaProperty.modelName), properties);
    }

    @Override
    protected SchemaCategory createModel() {

        SchemaCategory category = new SchemaCategory(name);
        category.expandedProperty().set(expanded);

        for(SSchemaProperty p: properties){
            category.addChild(p.convertToModel());
        }

        return category;
    }
}

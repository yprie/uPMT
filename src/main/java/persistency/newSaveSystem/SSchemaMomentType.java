package persistency.newSaveSystem;

import models.SchemaCategory;
import models.SchemaMomentType;
import org.json.JSONException;
import persistency.newSaveSystem.serialization.ObjectSerializer;

import java.util.ArrayList;


public class SSchemaMomentType extends SSchemaElement<SchemaMomentType> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaMomentType";

    //Fields
    public ArrayList<SSchemaCategory> categories;
    public String color;
    public boolean transitional;


    public SSchemaMomentType(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaMomentType(ObjectSerializer serializer, SchemaMomentType modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(SchemaMomentType modelReference) {
        super.init(modelReference);
        this.color = modelReference.getColor();
        this.transitional = modelReference.getTransitional();
        this.categories = new ArrayList<>();
        for (SchemaCategory sc : modelReference.categoriesProperty()) {
            this.categories.add(new SSchemaCategory(serializer, sc));
        }
    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        super.read();
        versionCheck(version, serializer.getInt("@version"));
        categories = serializer.getArray(serializer.setListSuffix(SSchemaCategory.modelName), SSchemaCategory::new);
        try {
            color = serializer.getString("color");
        } catch (JSONException error) {
            color = "ffffff";
        }
        try {
            transitional = serializer.getBoolean("transitional");
        } catch (JSONException error) {
            transitional = false;
        }
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        super.write(serializer);
        serializer.writeArray(serializer.setListSuffix(SSchemaCategory.modelName), categories);
        serializer.writeString("color", color);
        serializer.writeBoolean("transitional", transitional);
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected SchemaMomentType createModel() {
        SchemaMomentType schemaMomentType = new SchemaMomentType(name, color, transitional);
        schemaMomentType.expandedProperty().set(expanded);

        for (SSchemaCategory sc : categories) {
            schemaMomentType.addChild(sc.convertToModel());
        }

        return schemaMomentType;
    }

}

package persistency.newSaveSystem;

import models.*;
import org.json.JSONException;
import persistency.newSaveSystem.serialization.ObjectSerializer;

import java.util.ArrayList;


public class SSchemaTreeRoot extends SSchemaElement<SchemaTreeRoot> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaTreeRoot";

    //Fields
    public ArrayList<SSchemaFolder> folders;
    public ArrayList<SSchemaCategory> categories;
    public ArrayList<SSchemaMomentType> momentTypes;

    public SSchemaTreeRoot(ObjectSerializer serializer) {
        super(serializer);
    }
    public SSchemaTreeRoot(ObjectSerializer serializer, SchemaTreeRoot modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(SchemaTreeRoot modelReference) {
        super.init(modelReference);

        // WARNING : ORDER IS REALLY IMPORTANT : categories needs to be saved before entering a folder and momentTypes after a folder
        this.categories = new ArrayList<>();
        for (SchemaCategory c : modelReference.categoriesProperty()) {
            categories.add(new SSchemaCategory(serializer, c));
        }

        this.folders = new ArrayList<>();
        for(SchemaFolder f: modelReference.foldersProperty()) {
            folders.add(new SSchemaFolder(serializer, f));
        }

        this.momentTypes = new ArrayList<>();
        for (SchemaMomentType mt: modelReference.momentTypesProperty()) {
            momentTypes.add(new SSchemaMomentType(serializer, mt));
        }

    }
    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        super.read();
        versionCheck(version, serializer.getInt("@version"));

        try {
            folders = serializer.getArray(serializer.setListSuffix(SSchemaFolder.modelName), SSchemaFolder::new);
        } catch (JSONException error) {
            folders = new ArrayList<>();
        }

        try {
            categories = serializer.getArray(serializer.setListSuffix(SSchemaCategory.modelName), SSchemaCategory::new);
        } catch (JSONException error) {
            categories = new ArrayList<>();
        }

        try {
            momentTypes = serializer.getArray(serializer.setListSuffix(SSchemaMomentType.modelName), SSchemaMomentType::new);
        } catch (JSONException error) {
            momentTypes = new ArrayList<>();
        }
    }

    @Override
    public void write(ObjectSerializer serializer) {
        super.write(serializer);
        serializer.writeArray(serializer.setListSuffix(SSchemaFolder.modelName), folders);
        serializer.writeArray(serializer.setListSuffix(SSchemaCategory.modelName), categories);
        serializer.writeArray(serializer.setListSuffix(SSchemaMomentType.modelName), momentTypes);
    }

    @Override
    protected SchemaTreeRoot createModel() {
        SchemaTreeRoot root = new SchemaTreeRoot(name);
        root.expandedProperty().set(expanded);

        for(SSchemaFolder f: folders){
            root.addChild(f.convertToModel());
        }

        for(SSchemaCategory c: categories){
            root.addChild(c.convertToModel());
        }

        for(SSchemaMomentType mt: momentTypes) {
            root.addChild(mt.convertToModel());
        }
        return root;
    }

}


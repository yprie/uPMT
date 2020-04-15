package persistency.newSaveSystem;

import models.SchemaCategory;
import models.SchemaFolder;

import persistency.newSaveSystem.serialization.ObjectSerializer;

import java.util.ArrayList;


public class SSchemaFolder extends SSchemaElement<SchemaFolder> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaFolder";

    //Fields
    public ArrayList<SSchemaFolder> folders;
    public ArrayList<SSchemaCategory> categories;

    public SSchemaFolder(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaFolder(SchemaFolder modelReference) {
        super(modelName, version, modelReference);

        this.folders = new ArrayList<>();
        for(SchemaFolder f: modelReference.foldersProperty()) {
            folders.add(new SSchemaFolder(f));
        }

        this.categories = new ArrayList<>();
        for(SchemaCategory c: modelReference.categoriesProperty()) {
            categories.add(new SSchemaCategory(c));
        }
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        super.read();
        folders = serializer.getArray(serializer.setListSuffix(SSchemaFolder.modelName), SSchemaFolder::new);
        categories = serializer.getArray(serializer.setListSuffix(SSchemaCategory.modelName), SSchemaCategory::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        super.write(serializer);
        serializer.writeArray(serializer.setListSuffix(SSchemaFolder.modelName), folders);
        serializer.writeArray(serializer.setListSuffix(SSchemaCategory.modelName), categories);
    }

    @Override
    protected SchemaFolder createModel() {
        SchemaFolder folder = new SchemaFolder(name);
        folder.expandedProperty().set(expanded);

        for(SSchemaFolder f: folders){
            folder.addChild(f.convertToModel());
        }

        for(SSchemaCategory c: categories){
            folder.addChild(c.convertToModel());
        }

        return folder;
    }
}

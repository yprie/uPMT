package persistency.newSaveSystem;

import models.SchemaFolder;
import models.SchemaTreeRoot;
import persistency.newSaveSystem.serialization.ObjectSerializer;

import java.util.ArrayList;


public class SSchemaTreeRoot extends SSchemaElement<SchemaTreeRoot> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaTreeRoot";

    //Fields
    public ArrayList<SSchemaFolder> folders;

    public SSchemaTreeRoot(ObjectSerializer serializer) {
        super(serializer);
    }
    public SSchemaTreeRoot(SchemaTreeRoot modelReference) {
        super(modelName, version, modelReference);

        this.folders = new ArrayList<>();
        for(SchemaFolder f: modelReference.foldersProperty()) {
            folders.add(new SSchemaFolder(f));
        }
    }


    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        super.read();
        folders = serializer.getArray(serializer.setListSuffix(SSchemaFolder.modelName), SSchemaFolder::new);
    }

    @Override
    public void write(ObjectSerializer serializer) {
        super.write(serializer);
        serializer.writeArray(serializer.setListSuffix(SSchemaFolder.modelName), folders);
    }

    @Override
    protected SchemaTreeRoot createModel() {
        SchemaTreeRoot root = new SchemaTreeRoot(name);
        root.expandedProperty().set(expanded);

        for(SSchemaFolder f: folders){
            root.addChild(f.convertToModel());
        }
        return root;
    }

}


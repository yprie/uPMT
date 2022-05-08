package persistency.newSaveSystem;

import components.toolbox.models.SchemaMomentType;
import persistency.newSaveSystem.serialization.ObjectSerializer;


public class SSchemaMomentType extends SSchemaElement<SchemaMomentType> {

    //General info
    public static final int version = 1;
    public static final String modelName = "schemaMomentType";


    public SSchemaMomentType(ObjectSerializer serializer) {
        super(serializer);
    }

    public SSchemaMomentType(ObjectSerializer serializer, SchemaMomentType modelReference) {
        super(serializer, modelName, version, modelReference);
    }

    @Override
    public void init(SchemaMomentType modelReference) {
        super.init(modelReference);
    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        super.read();
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        super.write(serializer);
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected SchemaMomentType createModel() {
        SchemaMomentType schemaMomentType = new SchemaMomentType(name);
        schemaMomentType.expandedProperty().set(expanded);
        return schemaMomentType;
    }

}

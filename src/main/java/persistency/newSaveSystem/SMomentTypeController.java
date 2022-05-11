package persistency.newSaveSystem;

import components.toolbox.controllers.MomentTypeController;
import models.SchemaMomentType;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SMomentTypeController extends Serializable<MomentTypeController> {

    //General info
    public static final int version = 1;
    public static final String modelName = "momentTypeController";

    public SSchemaMomentType schemaMomentType;

    public SMomentTypeController(ObjectSerializer serializer) {
        super(serializer);
    }

    public SMomentTypeController(ObjectSerializer serializer, MomentTypeController objectReference) {
        super(serializer, modelName, version, objectReference);
    }

    @Override
    public void init(MomentTypeController modelReference) {
        schemaMomentType = new SSchemaMomentType(serializer, (SchemaMomentType) modelReference.getSchemaMomentType());
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        if (!serializer.getExists("schemaMomentType")) {
            schemaMomentType = new SSchemaMomentType(serializer);
            serializer.writeObject("schemaMomentType", schemaMomentType);
        }
        schemaMomentType = serializer.getObject("schemaMomentType", SSchemaMomentType::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeObject("schemaMomentType", schemaMomentType);
    }

    @Override
    protected MomentTypeController createModel() {
        return new MomentTypeController(schemaMomentType.convertToModel());
    }
}

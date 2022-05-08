package persistency.newSaveSystem;

import components.toolbox.controllers.MomentTypeController;
import components.toolbox.models.SchemaMomentType;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

public class SMomentTypeController extends Serializable<MomentTypeController> {

    //General info
    public static final int version = 1;
    public static final String modelName = "momentTypeController";

    public SSchemaMomentType schemaMomentType;
    public SMomentType momentType;

    public SMomentTypeController(ObjectSerializer serializer) {
        super(serializer);
    }

    public SMomentTypeController(ObjectSerializer serializer, MomentTypeController objectReference) {
        super(serializer, modelName, version, objectReference);
    }

    @Override
    public void init(MomentTypeController modelReference) {
        schemaMomentType = new SSchemaMomentType(serializer, (SchemaMomentType) modelReference.getSchemaMomentType());
        momentType = new SMomentType(serializer, modelReference.getMomentType());
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
        if (!serializer.getExists("momentType")) {
            momentType = new SMomentType(serializer);
            serializer.writeObject("momentType", momentType);
        }
        schemaMomentType = serializer.getObject("schemaMomentType", SSchemaMomentType::new);
        momentType = serializer.getObject("momentType", SMomentType::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeObject("schemaMomentType", schemaMomentType);
        serializer.writeObject("momentType", momentType);
    }

    @Override
    protected MomentTypeController createModel() {
        return new MomentTypeController(schemaMomentType.convertToModel(), momentType.convertToModel());
    }
}

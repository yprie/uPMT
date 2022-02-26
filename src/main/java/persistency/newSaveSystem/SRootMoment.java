package persistency.newSaveSystem;

import models.Moment;
import models.RootMoment;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SRootMoment extends Serializable<RootMoment> {

    //General info
    public static final int version = 1;
    public static final String modelName = "rootMoment";

    public ArrayList<SMoment> submoments;

    public SRootMoment(ObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public void init(RootMoment modelReference) {
        this.submoments = new ArrayList<>();
        for(Moment m: modelReference.momentsProperty())
            submoments.add(new SMoment(serializer ,m));
    }

    public SRootMoment(ObjectSerializer serializer, RootMoment modelReference) {
        super(serializer, modelName, version, modelReference);
    }


    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        submoments = serializer.getArray(serializer.setListSuffix(SMoment.modelName), SMoment::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeArray(serializer.setListSuffix(SMoment.modelName), submoments);
    }

    @Override
    protected RootMoment createModel() {
        RootMoment rm = new RootMoment();

        for(SMoment sm: submoments)
            rm.addMoment(sm.convertToModel());
        for(Moment sm: rm.momentsProperty())
            sm.addParent(rm);
        return rm;
    }
}

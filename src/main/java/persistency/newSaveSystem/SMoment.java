package persistency.newSaveSystem;

import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SMoment extends Serializable<Moment> {

    //General info
    public static final int version = 1;
    public static final String modelName = "moment";

    //Fields
    public String name;
    public ArrayList<SMoment> submoments;

    public SMoment(ObjectSerializer serializer) {
        super(serializer);
    }
    public SMoment(Moment objectReference) {
        super(modelName, version, objectReference);

        this.name = objectReference.getName();

        this.submoments = new ArrayList<>();
        for(Moment m: objectReference.momentsProperty())
            submoments.add(new SMoment(m));
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        name = serializer.getString("name");
        submoments = serializer.getArray(serializer.setListSuffix(SMoment.modelName), SMoment::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeArray(serializer.setListSuffix(SMoment.modelName), submoments);

    }

    @Override
    protected Moment createModel() {
        Moment m = new Moment(name);
        for(SMoment sm: submoments)
            m.addMoment(sm.convertToModel());
        return m;
    }
}

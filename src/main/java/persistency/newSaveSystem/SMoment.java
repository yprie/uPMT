package persistency.newSaveSystem;

import models.ConcreteCategory;
import models.Moment;
import org.json.JSONException;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SMoment extends Serializable<Moment> {

    //General info
    public static final int version = 1;
    public static final String modelName = "moment";

    //Fields
    public String name;
    public String comment;
    public boolean isCommentVisible;
    public boolean isCollapsed;
    public SJustification justification;
    public ArrayList<SConcreteCategory> categories;
    public ArrayList<SMoment> submoments;

    public SMoment(ObjectSerializer serializer) {
        super(serializer);
    }

    public SMoment(ObjectSerializer serializer, Moment objectReference) {
        super(serializer, modelName, version, objectReference);
    }

    @Override
    public void init(Moment modelReference) {
        this.name = modelReference.getName();
        this.comment = modelReference.getComment();
        this.isCommentVisible = modelReference.isCommentVisible();
        this.isCollapsed = modelReference.isCollapsed();
        this.justification = new SJustification(serializer, modelReference.getJustification());

        this.categories = new ArrayList<>();
        for(ConcreteCategory cc: modelReference.concreteCategoriesProperty())
            categories.add(new SConcreteCategory(serializer, cc));

        this.submoments = new ArrayList<>();
        for(Moment m: modelReference.momentsProperty())
            submoments.add(new SMoment(serializer, m));
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        name = serializer.getString("name");
        comment = serializer.getFacultativeString("momentComment",null);
        isCommentVisible = serializer.getBoolean("isCommentVisible");
        try {
            isCollapsed = serializer.getBoolean("isCollapsed");
        } catch (JSONException error) {
            isCollapsed = false;
        }
        justification = serializer.getObject("justification", SJustification::new);
        categories = serializer.getArray(serializer.setListSuffix(SConcreteCategory.modelName), SConcreteCategory::new);
        submoments = serializer.getArray(serializer.setListSuffix(SMoment.modelName), SMoment::new);
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeFacultativeString("momentComment",comment);
        serializer.writeBoolean("isCommentVisible", isCommentVisible);
        serializer.writeBoolean("isCollapsed", isCollapsed);
        serializer.writeObject("justification", justification);
        serializer.writeArray(serializer.setListSuffix(SConcreteCategory.modelName), categories);
        serializer.writeArray(serializer.setListSuffix(SMoment.modelName), submoments);
    }

    @Override
    protected Moment createModel() {
        Moment m = new Moment(name, comment, isCommentVisible, justification.createModel(), isCollapsed);
        for(SMoment sm: submoments)
            m.addMoment(sm.convertToModel());
        for(SConcreteCategory cc: categories)
            m.addCategory(cc.convertToModel());
        return m;
    }
}

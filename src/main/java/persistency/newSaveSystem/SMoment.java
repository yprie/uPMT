package persistency.newSaveSystem;

import models.ConcreteCategory;
import models.Moment;
import org.json.JSONException;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;

public class SMoment extends Serializable<Moment> {

    //General info
    public static final int version = 3;
    public static final String modelName = "moment";

    //Fields
    public String name;
    public String comment;
    public boolean isCommentVisible;
    public boolean isCollapsed;
    public SJustification justification;
    public ArrayList<SConcreteCategory> categories;
    public ArrayList<SMoment> submoments;
    public boolean transitional;
    public String color;

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
        this.transitional = modelReference.getTransitional();
        this.color = modelReference.getColor();

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
        versionCheck(version, serializer.getInt("@version"));
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

        try {
            transitional = serializer.getBoolean("transitional");
        } catch (JSONException error) {
            transitional = false;
        }

        try {
            color = serializer.getString("color");
        } catch (JSONException error) {
            color = "000000";
        }
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
        serializer.writeBoolean("transitional", transitional);
        serializer.writeString("color", color);
    }

    @Override
    protected Moment createModel() {
        Moment m = new Moment(name, comment, isCommentVisible, justification.createModel(), isCollapsed, transitional, color);
        for(SMoment sm: submoments)
            m.addMoment(sm.convertToModel());
        for (Moment sm : m.momentsProperty())
            sm.addParent(m);
        for(SConcreteCategory cc: categories)
            m.addCategory(cc.convertToModel());

        return m;
    }
}

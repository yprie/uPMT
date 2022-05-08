package persistency.newSaveSystem;

import components.toolbox.models.MomentType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import models.ConcreteCategory;
import persistency.newSaveSystem.serialization.ObjectSerializer;
import persistency.newSaveSystem.serialization.Serializable;

import java.util.ArrayList;
import java.util.LinkedList;

public class SMomentType extends Serializable<MomentType> {

    //General info
    public static final int version = 1;
    public static final String modelName = "momentTypeController";

    public String name;
    private SJustification justification;
    private ArrayList<SConcreteCategory> categories;
    private String comment;
    private boolean commentVisible;
    private boolean collapsed;
    private boolean transitional;
    private String color = "ffffff";

    public SMomentType(ObjectSerializer serializer) {
        super(serializer);
    }

    public SMomentType(ObjectSerializer serializer, MomentType objectReference) {
        super(serializer, modelName, version, objectReference);
    }

    @Override
    public void init(MomentType modelReference) {
        this.name = modelReference.getName();
        this.justification = new SJustification(serializer, modelReference.getJustification());
        this.categories = new ArrayList<>();
        for (ConcreteCategory cc : modelReference.categoriesProperty()) {
            this.categories.add(new SConcreteCategory(serializer, cc));
        }
        this.comment = modelReference.getComment();
        this.commentVisible = modelReference.isCommentVisible();
        this.collapsed = modelReference.isCollapsed();
        this.transitional = modelReference.isTransitional();
        this.color = modelReference.getColor();
    }

    @Override
    protected void addStrategies() {

    }

    @Override
    protected void read() {
        versionCheck(version, serializer.getInt("@version"));
        try {
            name = serializer.getString("name");
        } catch (Exception e) {
            serializer.writeString("name", name);
        }
        try {
            justification = serializer.getObject("justification", SJustification::new);
        } catch (Exception e) {
            justification = new SJustification(serializer);
            serializer.writeObject("justification", justification);
        }
        try {
            categories = serializer.getArray(serializer.setListSuffix(SConcreteCategory.modelName), SConcreteCategory::new);
        } catch (Exception e) {
            categories = new ArrayList<>();
            serializer.writeArray(serializer.setListSuffix(SConcreteCategory.modelName), categories);
        }
        try {
            comment = serializer.getString("comment");
        } catch (Exception e) {
            serializer.writeString("comment", comment);
        }
        try {
            commentVisible = serializer.getBoolean("commentVisible");
        } catch (Exception e) {
            serializer.writeBoolean("commentVisible", commentVisible);
        }
        try {
            collapsed = serializer.getBoolean("collapsed");
        } catch (Exception e) {
            serializer.writeBoolean("collapsed", collapsed);
        }
        try {
            transitional = serializer.getBoolean("transitional");
        } catch (Exception e) {
            serializer.writeBoolean("transitional", transitional);
        }
        try {
            color = serializer.getString("color");
        } catch (Exception e) {
            serializer.writeString("color", color);
        }
    }

    @Override
    protected void write(ObjectSerializer serializer) {
        serializer.writeString("name", name);
        serializer.writeObject("justification", justification);
        serializer.writeArray(serializer.setListSuffix(SConcreteCategory.modelName), categories);
        serializer.writeString("comment", comment);
        serializer.writeBoolean("commentVisible", commentVisible);
        serializer.writeBoolean("collapsed", collapsed);
        serializer.writeBoolean("transitional", transitional);
        serializer.writeString("color", color);
    }

    @Override
    protected MomentType createModel() {
        ListProperty<ConcreteCategory> cats =  new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        for (SConcreteCategory scc : this.categories) {
            cats.add(scc.createModel());
        }
        return new MomentType(this.name, this.comment, this.commentVisible, this.justification.createModel(), this.collapsed, this.transitional, this.color, cats);
    }


}

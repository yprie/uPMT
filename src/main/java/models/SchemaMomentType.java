package models;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.input.DataFormat;
import utils.removable.IRemovable;

public class SchemaMomentType extends SchemaElement implements IRemovable {

    public static final DataFormat format = new DataFormat("SchemaMomentType");
    private SimpleBooleanProperty exists;
    private MomentType momentType;

    //Computed values, no need to store them
    private SimpleIntegerProperty nbUsesInModelisation;

    public SchemaMomentType(String name, MomentType momentType) {
        super(name);
        this.exists = new SimpleBooleanProperty(true);
        this.nbUsesInModelisation = new SimpleIntegerProperty(0);
        this.momentType = momentType;
    }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return false;
    }

    @Override
    public boolean hasChild(SchemaTreePluggable item) {
        return false;
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("(SchemaProperty::addChild) Can't receive this kind of child !");
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        throw new IllegalArgumentException("(SchemaProperty::addChild) Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("(SchemaProperty::addChild) Can't receive this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        throw new IllegalArgumentException("(SchemaProperty::addChild) Can't receive this kind of child !");
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean canChangeParent() {
        return false;
    }

    @Override
    public DataFormat getDataFormat() {
        return SchemaMomentType.format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public String getIconPath() {
        return "toolBox_icon.png";
    }

    @Override
    public void setExists(boolean b) {
        exists.set(b);
    }

    @Override
    public ObservableBooleanValue existsProperty() {
        return exists;
    }

    public void setNumberOfUsesInModelisation(int nbUses) { this.nbUsesInModelisation.set(nbUses); }

    public ReadOnlyIntegerProperty numberOfUsesInModelisationProperty() { return this.nbUsesInModelisation; }

    public MomentType getMomentType() {
        return momentType;
    }
}

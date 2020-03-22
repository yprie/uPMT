package models;


import javafx.beans.value.ObservableBooleanValue;
import utils.removable.IRemovable;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.scene.input.DataFormat;

public class SchemaProperty extends SchemaElement implements IRemovable {

    public static final DataFormat format = new DataFormat("SchemaProperty");
    private SimpleBooleanProperty exists;

    public SchemaProperty(String name) {
        super(name);
        this.exists = new SimpleBooleanProperty(true);
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
        throw new IllegalArgumentException("(SchemaProperty::addChildAt) Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("(SchemaProperty::removeChild) Can't receive this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        throw new IllegalArgumentException("(SchemaProperty) The provided item is not a child of this element!");
    }

    @Override
    public DataFormat getDataFormat() { return format; }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) { visitor.visit(this); }

    @Override
    public boolean canChangeParent() {
        return false;
    }

    @Override
    public String getIconPath() {
        return "property.png";
    }

    @Override
    public void setExists(boolean b) {
        exists.set(b);
    }

    @Override
    public ObservableBooleanValue existsProperty() {
        return exists;
    }

}

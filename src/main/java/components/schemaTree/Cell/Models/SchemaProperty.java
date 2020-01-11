package components.schemaTree.Cell.Models;


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
    public void addChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        throw new IllegalArgumentException("The provided item is not a child of this element!");
    }

    @Override
    public DataFormat getDataFormat() { return format; }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) { visitor.visit(this); }

    @Override
    public String getIconPath() {
        return "property.png";
    }

    @Override
    public BooleanProperty existsProperty() {
        return exists;
    }

}

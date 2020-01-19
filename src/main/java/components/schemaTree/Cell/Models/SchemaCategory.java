package components.schemaTree.Cell.Models;

import utils.removable.IRemovable;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class SchemaCategory extends SchemaElement implements IRemovable {

    public static final DataFormat format = new DataFormat("SchemaCategory");

    private SimpleBooleanProperty exists;
    private ListProperty<SchemaProperty> properties;

    public SchemaCategory(String name) {
        super(name);
        this.exists = new SimpleBooleanProperty(true);
        this.properties = new SimpleListProperty<SchemaProperty>(FXCollections.observableList(new LinkedList<SchemaProperty>()));
    }

    public final ObservableList<SchemaProperty> propertiesProperty() { return properties; }

    @Override
    public BooleanProperty existsProperty() { return exists; }

    @Override
    public DataFormat getDataFormat() {
        return SchemaCategory.format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public String getIconPath() {
        return "category.png";
    }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return Utils.IsSchemaTreeProperty(item);
    }

    @Override
    public boolean hasChild(SchemaTreePluggable item) {
        return this.properties.indexOf(item) != -1;
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeProperty(item))
            addProperty((SchemaProperty)item, -1);
        else
            throw new IllegalArgumentException("(SchemaCategory::addChild) Can't receive this kind of child !");
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        if(Utils.IsSchemaTreeProperty(item))
            addProperty((SchemaProperty)item, index);
        else
            throw new IllegalArgumentException("(SchemaCategory::addChildAt) Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeProperty(item))
            removeProperty((SchemaProperty)item);
        else
            throw new IllegalArgumentException("(SchemaCategory::removeChild) Can't remove this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        int r = this.properties.indexOf(item);
        if(r == -1)
            throw new IllegalArgumentException("(SchemaCategory) The provided item is not a child of this element!");
        return r;
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean canChangeParent() {
        return true;
    }

    private void addProperty(SchemaProperty p, int index){
        if(index == -1)
            properties.add(p);
        else
            properties.add(index, p);
    }
    private void removeProperty(SchemaProperty p){
        properties.remove(p);
    }
}

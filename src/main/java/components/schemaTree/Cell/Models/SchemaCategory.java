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
    public void addChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeProperty(item))
            addProperty((SchemaProperty)item);
        else
            throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeProperty(item))
            removeProperty((SchemaProperty)item);
        else
            throw new IllegalArgumentException("Can't remove this kind of child !");
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    private void addProperty(SchemaProperty p){
        properties.add(p);
        Utils.setupListenerOnChildRemoving(this, p);
    }
    private void removeProperty(SchemaProperty p){
        properties.remove(p);
    }
}

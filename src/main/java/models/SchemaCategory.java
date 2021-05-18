package models;

import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import javafx.beans.value.ObservableBooleanValue;
import utils.removable.IRemovable;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SchemaCategory extends SchemaElement implements IRemovable {

    public static final DataFormat format = new DataFormat("SchemaCategory");

    private SimpleBooleanProperty exists;
    private ListProperty<SchemaProperty> properties;

    //Computed values, no need to store them
    private SimpleIntegerProperty nbUsesInModelisation;
    private SimpleStringProperty color = new SimpleStringProperty("efe4b0");
    private ArrayList<ConcreteCategoryController> ListImplements = new ArrayList<>();

    public SchemaCategory(String name) {
        super(name);
        this.exists = new SimpleBooleanProperty(true);
        this.properties = new SimpleListProperty<SchemaProperty>(FXCollections.observableList(new LinkedList<SchemaProperty>()));
        this.nbUsesInModelisation = new SimpleIntegerProperty(0);
    }

    public SchemaCategory(String name, String color) {
        super(name);
        this.exists = new SimpleBooleanProperty(true);
        this.properties = new SimpleListProperty<SchemaProperty>(FXCollections.observableList(new LinkedList<SchemaProperty>()));
        this.nbUsesInModelisation = new SimpleIntegerProperty(0);
        this.color = new SimpleStringProperty(color);
    }

    public final ObservableList<SchemaProperty> propertiesProperty() { return properties; }

    @Override
    public void setExists(boolean b) { exists.set(b); }

    @Override
    public ObservableBooleanValue existsProperty() { return exists; }

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

    public void setNumberOfUsesInModelisation(int nbUses) { this.nbUsesInModelisation.set(nbUses); }
    public ReadOnlyIntegerProperty numberOfUsesInModelisationProperty() { return this.nbUsesInModelisation; }


    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
        for(ConcreteCategoryController c: ListImplements) {
            c.updateColor();
        }
    }

    public void addToControllers(ConcreteCategoryController newController){
        ListImplements.add(newController);
    }
    public void removeFromControllers(ConcreteCategoryController Controller){
        ListImplements.remove(Controller);
    }
}

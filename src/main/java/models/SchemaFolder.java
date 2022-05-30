package models;

import javafx.beans.value.ObservableBooleanValue;
import utils.removable.IRemovable;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class SchemaFolder extends SchemaElement implements IRemovable {

    public static final DataFormat format = new DataFormat("SchemaFolder");
    private SimpleBooleanProperty exists;

    public ListProperty<SchemaTreePluggable> children;
    private ListProperty<SchemaCategory> categories;
    private ListProperty<SchemaFolder> folders;
    private ListProperty<SchemaMomentType> momentTypes;

    public SchemaFolder(String name) {
        super(name);
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.momentTypes = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.folders = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        this.exists = new SimpleBooleanProperty(true);

        this.children = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public final ObservableList<SchemaCategory> categoriesProperty() { return categories; }
    public final ObservableList<SchemaFolder> foldersProperty() { return folders; }
    public final ObservableList<SchemaMomentType> momentTypesProperty() { return momentTypes; }
    public final ObservableList<SchemaTreePluggable> childrenProperty() { return children; }

    @Override
    public DataFormat getDataFormat() {
        return SchemaFolder.format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return (Utils.IsSchemaTreeCategory(item) || Utils.IsSchemaTreeFolder(item) || Utils.IsSchemaTreeMomentType(item));
    }

    @Override
    public boolean hasChild(SchemaTreePluggable item) {
        return this.children.contains(item);
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        addChildAt(item, -1);
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        if(Utils.IsSchemaTreeCategory(item))
            addCategory((SchemaCategory) item, index);
        else if(Utils.IsSchemaTreeFolder(item))
            addFolder((SchemaFolder) item, index);
        else if(Utils.IsSchemaTreeMomentType(item))
            addMomentType((SchemaMomentType) item, index);
        else
            throw new IllegalArgumentException("(SchemaFolder::addChildAt) Can't receive this kind of child ! ");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeCategory(item))
            removeCategory((SchemaCategory) item);
        else if(Utils.IsSchemaTreeFolder(item))
            removeFolder((SchemaFolder) item);
        else if(Utils.IsSchemaTreeMomentType(item))
            removeMomentType((SchemaMomentType) item);
        else
            throw new IllegalArgumentException("(SchemaFolder::removeChild) Can't remove this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        int r = this.folders.indexOf(item);
        if(r == -1) {
            r = this.categories.indexOf(item);
        }
        if(r == -1) {
            r = this.momentTypes.indexOf(item);
        }
        if(r == -1)
            throw new IllegalArgumentException("(SchemaFolder::getChildIndex) The provided item is not a child of this element!");
        return r;
    }

    @Override
    public String getIconPath() {
        return "folder.png";
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean canChangeParent() {
        return true;
    }

    @Override
    public void setExists(boolean b) {
       exists.set(b);
       for(SchemaFolder f: folders) f.setExists(b);
       for(SchemaCategory c: categories) c.setExists(b);
       for(SchemaMomentType mt : momentTypes) mt.setExists(b);
    }

    @Override
    public ObservableBooleanValue existsProperty() {
        return exists;
    }

    private void addCategory(SchemaCategory c, int index){
        if(index == -1) {
            categories.add(c);
            children.add(folders.size() + categories.size()-1, c);
        }
        else {
            categories.add(index, c);
            children.add(folders.size() + index, c);
        }
    }
    private void removeCategory(SchemaCategory c){
        categories.remove(c);
        children.remove(c);
    }

    private void addFolder(SchemaFolder f, int index){
        if(index == -1) {
            folders.add(f);
            children.add(folders.size()-1 ,f);
        }
        else {
            folders.add(index, f);
            children.add(index ,f);
        }
    }
    private void removeFolder(SchemaFolder f){
        folders.remove(f);
        children.remove(f);
    }

    private void addMomentType(SchemaMomentType mt, int index) {
        if (index == -1) {
            momentTypes.add(mt);
            children.add(mt);
        }
        else {
            momentTypes.add(index, mt);
            children.add(folders.size() + categories.size() +index, mt);
        }
    }
    private void removeMomentType(SchemaMomentType mt){
        momentTypes.remove(mt);
        children.remove(mt);
    }


}

package components.schemaTree.Cell.Models;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import utils.autoSuggestion.AutoSuggestions;

import java.util.LinkedList;

public class SchemaTreeRoot extends SchemaElement {

    public static final DataFormat format = new DataFormat("SchemaTreeRoot");
    private ListProperty<SchemaFolder> folders;

    public SchemaTreeRoot(String name) {
        super(name);
        this.folders = new SimpleListProperty<SchemaFolder>(FXCollections.observableList(new LinkedList<SchemaFolder>()));
        AutoSuggestions.getAutoSuggestions().setSchemaTreeRoot(this);
    }

    public final ObservableList<SchemaFolder> foldersProperty() { return folders; }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return Utils.IsSchemaTreeFolder(item);
    }

    @Override
    public boolean hasChild(SchemaTreePluggable item) {
        return this.folders.indexOf(item) != -1;
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeFolder(item))
            addFolder((SchemaFolder)item, -1);
        else
            throw new IllegalArgumentException("(SchemaTreeRoot::addChild) Can't receive this kind of child !");
    }

    @Override
    public void addChildAt(SchemaTreePluggable item, int index) {
        if(Utils.IsSchemaTreeFolder(item))
            addFolder((SchemaFolder)item, index);
        else
            throw new IllegalArgumentException("(SchemaTreeRoot::addChildAt) Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeFolder(item))
            removeFolder((SchemaFolder)item);
        else
            throw new IllegalArgumentException("(SchemaTreeRoot::removeChild) Can't remove this kind of child !");
    }

    @Override
    public int getChildIndex(SchemaTreePluggable item) {
        int r = this.folders.get().indexOf(item);
        if(r == -1)
            throw new IllegalArgumentException("(SchemaTreeRoot) The provided item is not a child of this element!");
        return r;
    }

    @Override
    public String getIconPath() {
        return "schema.png";
    }

    @Override
    public DataFormat getDataFormat() {
        return SchemaTreeRoot.format;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public void accept(SchemaTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean canChangeParent() {
        return false;
    }

    public void addFolder(SchemaFolder f, int index){
        if(index == -1)
            folders.add(f);
        else
            folders.add(index, f);
    }
    public void removeFolder(SchemaFolder f){
        folders.remove(f);
    }

    @Override
    public String toString() { return getName(); }
}

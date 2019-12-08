package SchemaTree.Cell.Models;

import SchemaTree.Cell.Utils;
import SchemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class SchemaTreeRoot extends SchemaElement {

    public static final DataFormat format = new DataFormat("SchemaTreeRoot");
    private ListProperty<SchemaFolder> folders;

    public SchemaTreeRoot(String name) {
        super(name);
        this.folders = new SimpleListProperty<SchemaFolder>(FXCollections.observableList(new LinkedList<SchemaFolder>()));
    }

    public final ObservableList<SchemaFolder> foldersProperty() { return folders; }

    @Override
    public boolean canContain(SchemaTreePluggable item) {
        return Utils.IsSchemaTreeFolder(item);
    }

    @Override
    public void addChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeFolder(item))
            addFolder((SchemaFolder)item);
        else
            throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public void removeChild(SchemaTreePluggable item) {
        if(Utils.IsSchemaTreeFolder(item))
            removeFolder((SchemaFolder)item);
        else
            throw new IllegalArgumentException("Can't remove this kind of child !");
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

    public void addFolder(SchemaFolder f){
        folders.add(f);
        Utils.setupListenerOnChildRemoving(this, f);
    }
    public void removeFolder(SchemaFolder f){
        folders.remove(f);
        Utils.setupListenerOnChildRemoving(this, f);
    }
}

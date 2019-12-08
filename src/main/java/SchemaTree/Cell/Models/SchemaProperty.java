package SchemaTree.Cell.Models;


import NewModel.IDescriptemeAdapter;
import NewModel.IRemovable;
import SchemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import utils.ReactiveTree.ReactiveTreeElement;
import utils.ReactiveTree.ReactiveTreePluggable;
import javafx.scene.input.DataFormat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

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
    public void removeChild(SchemaTreePluggable item) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
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

package components.modelisationSpace.category.model;

import components.modelisationSpace.justification.models.Justification;
import components.schemaTree.Cell.Models.SchemaCategory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

public class ConcreteCategory implements IDraggable {

    public static final DataFormat format = new DataFormat("ConcreteCategory");
    private SchemaCategory category;
    private Justification justification;

    public ConcreteCategory(SchemaCategory c) {
        this.category = c;
        this.justification = new Justification();
    }

    public String getName() { return category.getName(); }
    public StringProperty nameProperty() { return category.nameProperty(); }

    public Justification getJustification() { return justification; }

    public boolean isSchemaCategory(SchemaCategory sc) { return sc == category; }
    public ObservableBooleanValue existsProperty() { return category.existsProperty(); }

    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }
}

package models;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

public abstract class TemplateMoment implements IDraggable {

    public static final DataFormat format = new DataFormat("TemplateMoment");
    private SimpleStringProperty templateName;

    public TemplateMoment(String templateName) {
        this.templateName = new SimpleStringProperty();
        this.templateName.set(templateName);;
    }

    public ReadOnlyStringProperty nameProperty() { return templateName; }

    abstract public Moment createConcreteMoment();

    public DataFormat getDataFormat() {
        return format;
    }

    public boolean isDraggable() {
        return true;
    }
}

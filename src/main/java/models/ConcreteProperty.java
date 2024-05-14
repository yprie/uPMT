package models;

import components.modelisationSpace.property.controllers.ConcretePropertyController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;

public class ConcreteProperty {

    private SchemaProperty property;
    private Justification justification;
    private StringProperty value;
    private ConcretePropertyController controller;

    public ConcreteProperty(SchemaProperty p) {
        this.property = p;
        this.value = new SimpleStringProperty("");
        this.justification = new Justification();
    }

    public ConcreteProperty(SchemaProperty p, Justification j) {
        this.property = p;
        this.value = new SimpleStringProperty("");
        this.justification = j;
    }

    public final SchemaProperty getSchemaProperty() {
        return property;
    }

    public String getName() {
        return property.getName();
    }

    public ObservableStringValue nameProperty() {
        return property.nameProperty();
    }

    public String getValue() {
        return value.get();
    }

    public ObservableStringValue valueProperty() {
        return value;
    }

    public void setValue(String s) {
        value.set(s);
    }

    public Justification getJustification() {
        return justification;
    }

    public boolean isSchemaProperty(SchemaProperty sp) {
        return sp == property;
    }

    public ConcretePropertyController getController() {
        return controller;
    }

    public void setController(ConcretePropertyController controller) {
        this.controller = controller;
    }

    // To string method
    @Override
    public String toString() {
        return property.getName() + " = " + value.getValue();
    }
}

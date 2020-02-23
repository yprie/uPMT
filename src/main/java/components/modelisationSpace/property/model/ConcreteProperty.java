package components.modelisationSpace.property.model;

import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.models.Justification;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;

public class ConcreteProperty {

    private SchemaProperty property;
    private Justification justification;
    private StringProperty value;

    public ConcreteProperty(SchemaProperty p) {
        this.property = p;
        this.value = new SimpleStringProperty("");
        this.justification = new Justification();
    }

    public String getName() { return property.getName(); }
    public ObservableStringValue nameProperty() { return property.nameProperty(); }

    public String getValue() { return value.get(); }
    public ObservableStringValue valueProperty() { return value; }
    public void setValue(String s) { value.set(s); }

    public Justification getJustification() { return justification; }

    public boolean isSchemaProperty(SchemaProperty sp) { return sp == property; }
}

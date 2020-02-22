package components.modelisationSpace.category.model;

import components.schemaTree.Cell.Models.SchemaCategory;
import javafx.beans.property.StringProperty;

public class ConcreteCategory {

    private SchemaCategory category;

    public ConcreteCategory(SchemaCategory c) {
        this.category = c;
    }

    public String getName() { return category.getName(); }
    public StringProperty nameProperty() { return category.nameProperty(); }

    public boolean isSchemaCategory(SchemaCategory sc) { return sc == category; }
}

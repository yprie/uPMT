package components.schemaTree.Services.propertyUsesCounter;

import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;

public class ResetPropertyUsesCounterVisitor extends SchemaTreePluggableVisitor {

    @Override
    public void visit(SchemaTreeRoot element) {
        element.foldersProperty().forEach(schemaFolder -> { schemaFolder.accept(this); });
    }

    @Override
    public void visit(SchemaFolder element) {
        element.foldersProperty().forEach(schemaFolder -> { schemaFolder.accept(this); });
        element.categoriesProperty().forEach(schemaCategory -> { schemaCategory.accept(this); });
    }

    @Override
    public void visit(SchemaCategory element) {
        element.propertiesProperty().forEach(schemaProperty -> { schemaProperty.accept(this); });
    }

    @Override
    public void visit(SchemaProperty element) {
        element.setNumberOfUsesInModelisation(0);
    }

}

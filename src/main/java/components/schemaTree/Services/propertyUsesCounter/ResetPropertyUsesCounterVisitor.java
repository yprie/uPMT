package components.schemaTree.Services.propertyUsesCounter;

import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import models.SchemaMomentType;
import models.*;

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

    @Override
    public void visit(SchemaMomentType element) {
        element.setNumberOfUsesInModelisation(0);
    }

}

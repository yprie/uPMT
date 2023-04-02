package components.schemaTree.Cell.Visitors;

import models.*;

public class InitTreeElement extends SchemaTreePluggableVisitor{
    @Override
    public void visit(SchemaTreeRoot element) {
        element.foldersProperty().forEach(schemaFolder -> { schemaFolder.accept(this); });
        element.categoriesProperty().forEach(schemaCategory -> { schemaCategory.accept(this); });
    }

    @Override
    public void visit(SchemaFolder element) {
        element.foldersProperty().forEach(schemaFolder -> { schemaFolder.accept(this); });
        element.categoriesProperty().forEach(schemaCategory -> { schemaCategory.accept(this); });
    }

    @Override
    public void visit(SchemaCategory element) {
        element.init();
    }

    @Override
    public void visit(SchemaProperty element) {

    }

    @Override
    public void visit(SchemaMomentType element) {

    }
}

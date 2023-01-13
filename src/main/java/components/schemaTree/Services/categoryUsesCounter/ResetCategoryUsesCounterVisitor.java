package components.schemaTree.Services.categoryUsesCounter;

import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;

public class ResetCategoryUsesCounterVisitor extends SchemaTreePluggableVisitor {

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
        element.setNumberOfUsesInModelisation(0);
    }

    @Override
    public void visit(SchemaProperty element) {

    }

}

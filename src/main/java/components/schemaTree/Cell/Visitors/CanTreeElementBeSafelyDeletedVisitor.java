package components.schemaTree.Cell.Visitors;

import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;

public class CanTreeElementBeSafelyDeletedVisitor extends SchemaTreePluggableVisitor {

    private boolean result = true;

    @Override
    public void visit(SchemaTreeRoot element) {
        result = false;
    }

    @Override
    public void visit(SchemaFolder element) {
        element.foldersProperty().forEach(schemaFolder -> { schemaFolder.accept(this); });
        element.categoriesProperty().forEach(schemaCategory -> { schemaCategory.accept(this); });
    }

    @Override
    public void visit(SchemaCategory element) {
        if(element.getNumberOfUsesInModelisation().get() > 0)
            result = false;
    }

    @Override
    public void visit(SchemaProperty element) {

    }

    public boolean elementCanBeSafelyDeleted() { return result; }
}

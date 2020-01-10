package components.schemaTree.Cell.Visitors;

import components.schemaTree.Cell.Controllers.*;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaProperty;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import components.schemaTree.Cell.SchemaTreePluggable;

public class CreateControllerVisitor extends SchemaTreePluggableVisitor {

    private SchemaTreeCellController resultController;
    private SchemaTreePluggable parent;

    public CreateControllerVisitor(SchemaTreePluggable parent) {
        this.parent = parent;
    }

    @Override
    public void visit(SchemaTreeRoot element) { resultController = new SchemaTreeRootController(element); }

    @Override
    public void visit(SchemaFolder element) {
        resultController = new SchemaTreeFolderController(parent, element);
    }

    @Override
    public void visit(SchemaCategory element) {
        resultController = new SchemaTreeCategoryController(parent, element);
    }

    @Override
    public void visit(SchemaProperty element) {
        resultController = new SchemaTreePropertyController(parent, element);
    }

    public SchemaTreeCellController getResultController() {
        return resultController;
    }
}

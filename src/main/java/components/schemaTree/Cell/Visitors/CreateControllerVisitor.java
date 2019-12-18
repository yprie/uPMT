package components.schemaTree.Cell.Visitors;

import components.schemaTree.Cell.Controllers.*;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaProperty;
import components.schemaTree.Cell.Models.SchemaTreeRoot;

public class CreateControllerVisitor extends SchemaTreePluggableVisitor {

    private SchemaTreeCellController resultController;

    @Override
    public void visit(SchemaTreeRoot element) {
        resultController = new SchemaTreeRootController(element);
    }

    @Override
    public void visit(SchemaFolder element) { resultController = new SchemaTreeFolderController(element); }

    @Override
    public void visit(SchemaCategory element) {
        resultController = new SchemaTreeCategoryController(element);
    }

    @Override
    public void visit(SchemaProperty element) {
        resultController = new SchemaTreePropertyController(element);
    }

    public SchemaTreeCellController getResultController() {
        return resultController;
    }
}

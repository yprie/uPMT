package SchemaTree.Cell.Visitors;

import SchemaTree.Cell.Controllers.*;
import SchemaTree.Cell.Models.SchemaCategory;
import SchemaTree.Cell.Models.SchemaFolder;
import SchemaTree.Cell.Models.SchemaProperty;
import SchemaTree.Cell.Models.SchemaTreeRoot;

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

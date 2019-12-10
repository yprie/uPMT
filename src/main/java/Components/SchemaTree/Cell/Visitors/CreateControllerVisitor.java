package Components.SchemaTree.Cell.Visitors;

import Components.SchemaTree.Cell.Controllers.*;
import Components.SchemaTree.Cell.Models.SchemaCategory;
import Components.SchemaTree.Cell.Models.SchemaFolder;
import Components.SchemaTree.Cell.Models.SchemaProperty;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;

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

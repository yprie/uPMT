package components.schemaTree.Cell.Visitors;

import components.schemaTree.Cell.Controllers.*;
import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;

public class CreateControllerVisitor extends SchemaTreePluggableVisitor {

    private SchemaTreeCellController resultController;
    private SchemaTreeCommandFactory cmdFactory;

    public CreateControllerVisitor(SchemaTreeCommandFactory cmdFactory) {
        this.cmdFactory = cmdFactory;
    }

    @Override
    public void visit(SchemaTreeRoot element) { resultController = new SchemaTreeRootController(element, cmdFactory); }

    @Override
    public void visit(SchemaFolder element) {
        resultController = new SchemaTreeFolderController(element, cmdFactory);
    }

    @Override
    public void visit(SchemaCategory element) {
        resultController = new SchemaTreeCategoryController(element, cmdFactory);
    }

    @Override
    public void visit(SchemaProperty element) {
        resultController = new SchemaTreePropertyController(element, cmdFactory);
    }

    public SchemaTreeCellController getResultController() {
        return resultController;
    }
}

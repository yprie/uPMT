package components.schemaTree.Cell.Visitors;

import components.schemaTree.Cell.Controllers.*;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaProperty;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

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

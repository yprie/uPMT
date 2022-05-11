package components.schemaTree.Cell.Visitors;

import models.SchemaMomentType;
import models.*;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.strategies.ContainerCreateChildStrategy;
import components.schemaTree.Cell.appCommands.strategies.LeafCreateChildStrategy;
import components.schemaTree.Cell.appCommands.AddChildStrategy;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CreateAddChildStrategyVisitor extends SchemaTreePluggableVisitor {

    private AddChildStrategy result;
    private TreeView<SchemaTreePluggable> view;
    private TreeItem<SchemaTreePluggable> item;
    private SchemaTreePluggable newModel;

    public CreateAddChildStrategyVisitor(TreeView<SchemaTreePluggable> view, TreeItem<SchemaTreePluggable> item, SchemaTreePluggable newModel) {
        this.view = view;
        this.item = item;
        this.newModel = newModel;
    }

    @Override
    public void visit(SchemaTreeRoot element) {
        result = new ContainerCreateChildStrategy(view, item, newModel);
    }

    @Override
    public void visit(SchemaFolder element) {
        result = new ContainerCreateChildStrategy(view, item, newModel);
    }

    @Override
    public void visit(SchemaCategory element) {
        result = new ContainerCreateChildStrategy(view, item, newModel);
    }

    @Override
    public void visit(SchemaProperty element) {
        result = new LeafCreateChildStrategy();
    }

    @Override
    public void visit(SchemaMomentType element) {
        result = new LeafCreateChildStrategy();
    }

    public AddChildStrategy getResultStrategy() { return result; }
}
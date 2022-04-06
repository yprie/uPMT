package components.schemaTree.Cell.Visitors;

import models.*;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.RemovingStrategy;
import components.schemaTree.Cell.appCommands.strategies.RemovableRemovingStrategy;
import components.schemaTree.Cell.appCommands.strategies.UnremovableRemovingStrategy;
import javafx.scene.control.TreeView;
import utils.removable.IRemovable;

public class CreateRemovingStrategyVisitor<E extends SchemaTreePluggable& IRemovable> extends SchemaTreePluggableVisitor {

    private RemovingStrategy result;

    private TreeView<SchemaTreePluggable> view;
    private SchemaTreePluggable parent;
    private E item;

    public  CreateRemovingStrategyVisitor(TreeView<SchemaTreePluggable> view, SchemaTreePluggable parent, E item) {
        this.view = view;
        this.parent = parent;
        this.item = item;
    }

    @Override
    public void visit(SchemaTreeRoot element) {
        result = new UnremovableRemovingStrategy<>();
    }

    @Override
    public void visit(SchemaFolder element) {
        result = new RemovableRemovingStrategy<>(view, parent, item);
    }

    @Override
    public void visit(SchemaCategory element) {
        result = new RemovableRemovingStrategy<>(view, parent, item);
    }

    @Override
    public void visit(SchemaProperty element) {
        result = new RemovableRemovingStrategy<>(view, parent, item);
    }

    @Override
    public void visit(SchemaMomentType element) {
        result = new RemovableRemovingStrategy<>(view, parent, item);
    }

    public RemovingStrategy getResultStrategy() {
        return result;
    }
}

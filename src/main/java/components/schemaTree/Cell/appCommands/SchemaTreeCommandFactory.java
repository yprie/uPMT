package components.schemaTree.Cell.appCommands;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Visitors.CreateAddChildStrategyVisitor;
import components.schemaTree.Cell.Visitors.CreateRemovingStrategyVisitor;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.removable.IRemovable;

public class SchemaTreeCommandFactory {

    TreeView<SchemaTreePluggable> view;
    TreeItem<SchemaTreePluggable> item;

    public SchemaTreeCommandFactory(TreeView<SchemaTreePluggable> view, TreeItem<SchemaTreePluggable> item) {
        this.view = view;
        this.item = item;
    }

    public AddChildStrategy addSchemaTreeChild(SchemaTreePluggable newModel) {
        CreateAddChildStrategyVisitor v = new CreateAddChildStrategyVisitor(view, item, newModel);
        item.getValue().accept(v);
        return v.getResultStrategy();
    }
    public <E extends SchemaTreePluggable&IRemovable> RemovingStrategy removeTreeElement(E element) {
        CreateRemovingStrategyVisitor v = new CreateRemovingStrategyVisitor<>(view, item.getParent().getValue(), element);
        element.accept(v);
        return v.getResultStrategy();
    }
}

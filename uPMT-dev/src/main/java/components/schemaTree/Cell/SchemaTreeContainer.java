package components.schemaTree.Cell;


import components.schemaTree.Cell.Visitors.CreateSchemaTreeItemVisitor;
import utils.reactiveTree.ReactiveTreeContainer;
import utils.reactiveTree.ReactiveTreeElement;

public class SchemaTreeContainer extends ReactiveTreeContainer<SchemaTreePluggable> {

    public SchemaTreeContainer(SchemaTreePluggable item) {
        super(item);
        setExpanded(item.expandedProperty().get());
        expandedProperty().bindBidirectional(item.expandedProperty());
    }

    @Override
    protected ReactiveTreeElement<SchemaTreePluggable> createTreeItem(SchemaTreePluggable item) {
        CreateSchemaTreeItemVisitor visitor = new CreateSchemaTreeItemVisitor();
        item.accept(visitor);
        return visitor.getSchemaTreeItem();
    }
}

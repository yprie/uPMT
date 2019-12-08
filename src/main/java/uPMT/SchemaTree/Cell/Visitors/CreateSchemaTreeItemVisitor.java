package SchemaTree.Cell.Visitors;

import SchemaTree.Cell.Models.*;
import utils.ReactiveTree.ReactiveTreeElement;

public class CreateSchemaTreeItemVisitor extends SchemaTreePluggableVisitor {

    private ReactiveTreeElement<SchemaTreePluggable> result;

    @Override
    public void visit(SchemaTreeRoot element) {
        SchemaTreeContainer item = new SchemaTreeContainer(element);
        item.bindChildrenCollection(element.foldersProperty());
        result = item;
    }

    @Override
    public void visit(SchemaFolder element) {
        SchemaTreeContainer item = new SchemaTreeContainer(element);
        item.bindChildrenCollection(element.foldersProperty());
        item.bindChildrenCollection(element.categoriesProperty());
        result = item;
    }

    @Override
    public void visit(SchemaCategory element) {
        SchemaTreeContainer item = new SchemaTreeContainer(element);
        item.bindChildrenCollection(element.propertiesProperty());
        result = item;
    }

    @Override
    public void visit(SchemaProperty element) {
        result = new ReactiveTreeElement<SchemaTreePluggable>(element);
    }

    public ReactiveTreeElement<SchemaTreePluggable> getSchemaTreeItem() {
        return result;
    }
}

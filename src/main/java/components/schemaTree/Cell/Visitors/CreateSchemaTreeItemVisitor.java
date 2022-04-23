package components.schemaTree.Cell.Visitors;

import components.schemaTree.Cell.SchemaTreeContainer;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Section;
import components.toolbox.models.SchemaMomentType;
import models.*;
import utils.reactiveTree.ReactiveTreeElement;

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
        /*item.bindChildrenCollection(element.foldersProperty());
        item.bindChildrenCollection(element.categoriesProperty());*/
        item.bindChildrenCollection(element.childrenProperty());
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

    @Override
    public void visit(SchemaMomentType element) {
        result = new ReactiveTreeElement<SchemaTreePluggable>(element);
    }

    public ReactiveTreeElement<SchemaTreePluggable> getSchemaTreeItem() {
        return result;
    }

    public Section mouseIsDraggingOn(double y) {
        if(y < 10) {
            return Section.bottom;
        }
        else if (y > 20){
            return Section.top;
        }
        else {
            return Section.middle;
        }
    }
}

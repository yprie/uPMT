package components.schemaTree.Cell.Visitors;

import models.*;

public class CanTreeElementBeSafelyRenamedVisitor extends SchemaTreePluggableVisitor {

    private boolean result = true;

    @Override
    public void visit(SchemaTreeRoot element) { }

    @Override
    public void visit(SchemaFolder element) { }

    @Override
    public void visit(SchemaCategory element) {
        if(element.numberOfUsesInModelisationProperty().get() > 0)
            result = false;
    }

    @Override
    public void visit(SchemaProperty element) {
        if(element.numberOfUsesInModelisationProperty().get() > 0)
            result = false;
    }

    @Override
    public void visit(SchemaMomentType element) {
        if(element.numberOfUsesInModelisationProperty().get() > 0)
            result = false;
    }

    public boolean elementCanBeSafelyRenamed() { return result; }
}

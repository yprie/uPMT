package components.schemaTree.Cell.Visitors;

import models.*;

public abstract class SchemaTreePluggableVisitor {

    public abstract void visit(SchemaTreeRoot element);
    public abstract void visit(SchemaFolder element);
    public abstract void visit(SchemaCategory element);
    public abstract void visit(SchemaProperty element);
    public abstract void visit(SchemaMomentType element);

}

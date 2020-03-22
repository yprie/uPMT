package components.schemaTree.Cell.Visitors;

import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;

public abstract class SchemaTreePluggableVisitor {

    public abstract void visit(SchemaTreeRoot element);
    public abstract void visit(SchemaFolder element);
    public abstract void visit(SchemaCategory element);
    public abstract void visit(SchemaProperty element);

}

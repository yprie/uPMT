package SchemaTree.Cell.Visitors;

import SchemaTree.Cell.Models.SchemaCategory;
import SchemaTree.Cell.Models.SchemaFolder;
import SchemaTree.Cell.Models.SchemaProperty;
import SchemaTree.Cell.Models.SchemaTreeRoot;

public abstract class SchemaTreePluggableVisitor {

    public abstract void visit(SchemaTreeRoot element);
    public abstract void visit(SchemaFolder element);
    public abstract void visit(SchemaCategory element);
    public abstract void visit(SchemaProperty element);

}

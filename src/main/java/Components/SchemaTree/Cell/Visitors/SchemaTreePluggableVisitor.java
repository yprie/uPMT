package Components.SchemaTree.Cell.Visitors;

import Components.SchemaTree.Cell.Models.SchemaCategory;
import Components.SchemaTree.Cell.Models.SchemaFolder;
import Components.SchemaTree.Cell.Models.SchemaProperty;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;

public abstract class SchemaTreePluggableVisitor {

    public abstract void visit(SchemaTreeRoot element);
    public abstract void visit(SchemaFolder element);
    public abstract void visit(SchemaCategory element);
    public abstract void visit(SchemaProperty element);

}

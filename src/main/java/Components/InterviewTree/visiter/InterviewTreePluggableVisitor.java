package Components.InterviewTree.visiter;

import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.SchemaTree.Cell.Models.SchemaCategory;
import Components.SchemaTree.Cell.Models.SchemaFolder;
import Components.SchemaTree.Cell.Models.SchemaProperty;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;

public abstract class InterviewTreePluggableVisitor {

    public abstract void visit(InterviewTreeRoot element);
    public abstract void visit(InterviewItem element);


}

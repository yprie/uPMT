package Components.InterviewTree;


import Components.InterviewTree.visiter.CreateInterviewTreeItemVisitor;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import Components.SchemaTree.Cell.Visitors.CreateSchemaTreeItemVisitor;
import utils.ReactiveTree.ReactiveTreeContainer;
import utils.ReactiveTree.ReactiveTreeElement;

public class InterviewTreeContainer extends ReactiveTreeContainer<InterviewTreePluggable> {

    public InterviewTreeContainer(InterviewTreePluggable item) {
        super(item);
//        setExpanded(item.expandedProperty().get());
//        expandedProperty().bindBidirectional(item.expandedProperty());
    }

    @Override
    protected ReactiveTreeElement<InterviewTreePluggable> createTreeItem(InterviewTreePluggable item) {
        CreateInterviewTreeItemVisitor visitor = new CreateInterviewTreeItemVisitor();
        item.accept(visitor);
        return visitor.getInterviewTreeElement();
    }
}

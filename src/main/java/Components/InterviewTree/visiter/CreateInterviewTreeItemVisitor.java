package Components.InterviewTree.visiter;

import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.InterviewTree.InterviewTreeContainer;
import Components.InterviewTree.InterviewTreePluggable;
import utils.ReactiveTree.ReactiveTreeElement;

public class CreateInterviewTreeItemVisitor extends InterviewTreePluggableVisitor {

    private ReactiveTreeElement<InterviewTreePluggable> result;

    @Override
    public void visit(InterviewTreeRoot element) {
        InterviewTreeContainer item = new InterviewTreeContainer(element);
        item.bindChildrenCollection(element.itemsProperty());
        result = item;
    }

    @Override
    public void visit(InterviewItem element) {
        result = new ReactiveTreeElement<InterviewTreePluggable>(element);

    }

    public ReactiveTreeElement<InterviewTreePluggable> getInterviewTreeElement() {
        return result;
    }
}

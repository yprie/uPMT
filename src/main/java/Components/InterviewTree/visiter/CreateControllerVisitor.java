package Components.InterviewTree.visiter;

import Components.InterviewTree.Cell.Model.InterviewItem;
import Components.InterviewTree.Cell.Model.InterviewTreeRoot;
import Components.InterviewTree.Controller.InterviewTreeCellController;
import Components.InterviewTree.Controller.InterviewTreeItemController;
import Components.InterviewTree.Controller.InterviewTreeRootController;

public class CreateControllerVisitor extends InterviewTreePluggableVisitor {

    private InterviewTreeCellController resultController;

    @Override
    public void visit(InterviewTreeRoot element) {
        resultController = new InterviewTreeRootController(element);
    }

    public void visit(InterviewItem element) { resultController = new InterviewTreeItemController(element); }

    public InterviewTreeCellController getResultController() {
        return resultController;
    }


}

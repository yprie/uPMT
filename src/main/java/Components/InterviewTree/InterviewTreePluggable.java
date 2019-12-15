package Components.InterviewTree;

import Components.InterviewTree.visiter.InterviewTreePluggableVisitor;
import utils.DragAndDrop.IDraggable;
import utils.ReactiveTree.ReactiveTreePluggable;

public interface InterviewTreePluggable extends ReactiveTreePluggable, IDraggable {

    boolean mustBeRenamed();
    void setMustBeRenamed(boolean YoN);


    void addChild(InterviewTreePluggable item);
    void removeChild(InterviewTreePluggable item);

    void accept(InterviewTreePluggableVisitor visitor);
}

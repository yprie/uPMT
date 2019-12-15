package Components.InterviewTree.Cell.Model;


import Components.InterviewSelector.Models.Interview;
import Components.InterviewTree.InterviewTreePluggable;
import Components.InterviewTree.visiter.InterviewTreePluggableVisitor;
import utils.Removable.IRemovable;
import javafx.beans.property.*;
import javafx.scene.input.DataFormat;

public class InterviewItem extends InterviewElement implements IRemovable {

    public static final DataFormat format = new DataFormat("InterviewItem");
    private SimpleBooleanProperty exists;
    private Interview interview;

    public InterviewItem(Interview interview) {
        super(interview.getTitle());
        this.exists = new SimpleBooleanProperty(true);
    }


    @Override
    public void addChild(InterviewTreePluggable item) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public void removeChild(InterviewTreePluggable item) {
        throw new IllegalArgumentException("Can't receive this kind of child !");
    }

    @Override
    public DataFormat getDataFormat() { return format; }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public void accept(InterviewTreePluggableVisitor visitor) { visitor.visit(this); }

    @Override
    public String getIconPath() {
        return "edit2.png";
    }

    @Override
    public BooleanProperty existsProperty() {
        return exists;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

}

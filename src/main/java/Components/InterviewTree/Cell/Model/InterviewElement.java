package Components.InterviewTree.Cell.Model;

import Components.InterviewTree.InterviewTreePluggable;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class InterviewElement implements InterviewTreePluggable {

    private boolean mustBeRenamed;
    private StringProperty name;

    InterviewElement(String name) {
        this.name = new SimpleStringProperty(name);
        this.mustBeRenamed = false;
    }

    @Override
    public boolean mustBeRenamed() {
        return mustBeRenamed;
    }

    public void setMustBeRenamed(boolean YoN) { mustBeRenamed = YoN; }

    @Override
    public StringProperty nameProperty() {
        return name;
    }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

}

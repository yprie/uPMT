package Components.SchemaTree.Cell.Models;
import Components.SchemaTree.Cell.SchemaTreePluggable;
import javafx.beans.property.*;

public abstract class SchemaElement implements SchemaTreePluggable {

    private boolean mustBeRenamed;
    private SimpleBooleanProperty expanded;
    private StringProperty name;

    SchemaElement(String name) {
        this.expanded = new SimpleBooleanProperty(true);
        this.name = new SimpleStringProperty(name);
        this.mustBeRenamed = false;
    }

    @Override
    public boolean mustBeRenamed() {
        return mustBeRenamed;
    }

    public void setMustBeRenamed(boolean YoN) { mustBeRenamed = YoN; }

    @Override
    public BooleanProperty expandedProperty() {
        return expanded;
    }
    @Override
    public boolean isExpanded() { return expanded.get(); }

    @Override
    public StringProperty nameProperty() {
        return name;
    }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

}

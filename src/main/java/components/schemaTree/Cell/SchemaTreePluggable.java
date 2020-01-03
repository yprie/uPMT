package components.schemaTree.Cell;

import components.schemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.BooleanProperty;
import utils.reactiveTree.ReactiveTreePluggable;
import utils.dragAndDrop.IDraggable;

public interface SchemaTreePluggable extends ReactiveTreePluggable, IDraggable {

    boolean mustBeRenamed();
    void setMustBeRenamed(boolean YoN);

    boolean isExpanded();
    BooleanProperty expandedProperty();

    boolean canContain(SchemaTreePluggable item);
    void addChild(SchemaTreePluggable item);
    void removeChild(SchemaTreePluggable item);

    void accept(SchemaTreePluggableVisitor visitor);
}

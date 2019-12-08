package SchemaTree.Cell.Models;

import SchemaTree.Cell.Visitors.SchemaTreePluggableVisitor;
import javafx.beans.property.BooleanProperty;
import utils.ReactiveTree.ReactiveTreePluggable;
import utils.UX.IDraggable;

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

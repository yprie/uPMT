package utils;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class Emphasable {
    SimpleBooleanProperty emphasize = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty getEmphasizeProperty() { return emphasize; }
}

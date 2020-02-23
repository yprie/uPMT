package utils.removable;

import javafx.beans.value.ObservableBooleanValue;

public interface IRemovable {
    void setExists(boolean b);
    ObservableBooleanValue existsProperty();
}

package utils.ReactiveTree;

import javafx.beans.property.StringProperty;

public interface ReactiveTreePluggable {

    StringProperty nameProperty();
    String getIconPath();

}

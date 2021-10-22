package utils.reactiveTree;

import javafx.beans.property.StringProperty;

public interface ReactiveTreePluggable {

    StringProperty nameProperty();
    String getIconPath();

}

package utils.reactiveTree;

import javafx.scene.control.TreeItem;

public class ReactiveTreeElement<T extends ReactiveTreePluggable> extends TreeItem<T> {
    public ReactiveTreeElement(T item) {
        setValue(item);
    }
}

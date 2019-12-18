package utils.reactiveTree;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.TreeItem;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ReactiveTreeContainer<T extends ReactiveTreePluggable> extends ReactiveTreeElement<T> {

    private SimpleBooleanProperty expanded;
    private ListChangeListener<T> onChildChangeListener = change -> {
        while (change.next()) {
            for (T removed : change.getRemoved()) {
                removeChildren(removed);
            }
            for (T added : change.getAddedSubList()) {
                addChildren(added);
            }
        }
    };;

    public ReactiveTreeContainer(T item) {
        super(item);
        expanded = new SimpleBooleanProperty(true);
        this.expandedProperty().bindBidirectional(expanded);
    }

    public void bindChildrenCollection(ObservableList<? extends T> children) {
        children.forEach(this::addChildren);
        children.addListener(new WeakListChangeListener<>(onChildChangeListener));
    }

    private void addChildren(T item) {
        getChildren().add(createTreeItem(item));
    }

    abstract protected ReactiveTreeElement<T> createTreeItem(T item);

    private void removeChildren(T item) {
        AtomicReference<TreeItem<T>> foundItem = new AtomicReference<TreeItem<T>>();
        getChildren().forEach(child -> {
            if(child.getValue() == item)
                foundItem.set(child);
        });
        if(foundItem.get() == null)
            throw new IllegalArgumentException("Trying to remove unexisting element from the container !");
        getChildren().remove(foundItem.get());
    }

}

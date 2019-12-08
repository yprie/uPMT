package utils.ReactiveTree;

import utils.ReactiveTree.ReactiveTreeElement;
import javafx.scene.control.TreeItem;

import java.util.Iterator;

public class LeafToRootIterator implements Iterator<TreeItem> {

    TreeItem element;

    public LeafToRootIterator(TreeItem element) { this.element = element; }

    @Override
    public boolean hasNext() {
        return element.getParent() != null;
    }

    @Override
    public TreeItem next() {
        element = element.getParent();
        return element;
    }

}

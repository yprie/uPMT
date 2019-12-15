package Components.InterviewTree.Cell.Model;

import Components.InterviewTree.InterviewTreePluggable;
import Components.InterviewTree.visiter.InterviewTreePluggableVisitor;
import Components.InterviewTree.Utils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;

import java.util.LinkedList;

public class InterviewTreeRoot extends InterviewElement {

    public static final DataFormat format = new DataFormat("InterviewTreeRoot");
    private ListProperty<InterviewItem> items;

    public InterviewTreeRoot(String name) {
        super(name);
        this.items = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public final ObservableList<InterviewItem> itemsProperty() { return items; }


    @Override
    public void addChild(InterviewTreePluggable item) {
            items.add((InterviewItem)item);
            Utils.setupListenerOnDelete(this,(InterviewItem)item);


    }

    @Override
    public void removeChild(InterviewTreePluggable item) {
        items.remove((InterviewItem)item);
        Utils.setupListenerOnDelete(this,(InterviewItem)item);
    }

    @Override
    public void accept(InterviewTreePluggableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getIconPath() {
        return "folder.png";
    }

    @Override
    public DataFormat getDataFormat() {
        return InterviewTreeRoot.format;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }



    @Override
    public String toString() { return getName(); }
}

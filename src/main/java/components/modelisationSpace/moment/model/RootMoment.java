package components.modelisationSpace.moment.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class RootMoment {

    private ListProperty<Moment> submoments;

    public RootMoment() {
        this.submoments = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public void addMoment(int index, Moment m) {
        submoments.add(index, m);
    }

    public void addMoment(Moment m) {
        submoments.add(m);
    }

    public void removeMoment(Moment m) {
        submoments.remove(m);
    }

    public ObservableList<Moment> momentsProperty() { return submoments; }

    public int indexOf(Moment m) {
        for(int i = 0; i < submoments.size(); i++){
            if(submoments.get(i) == m)
                return i;
        }
        return -1;
    }

}

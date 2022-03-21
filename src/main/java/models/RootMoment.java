package models;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Emphasable;

import java.util.LinkedList;

public class RootMoment extends Emphasable {
    private ListProperty<Moment> submoments;

    public RootMoment() {
        this.submoments = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public void addMoment(int index, Moment m) {
        submoments.add(index, m);
        m.setParent(this);
    }

    public void addMoment(Moment m) {
        submoments.add(m);
        m.setParent(this);

    }

    public void removeMoment(Moment m) {
        submoments.remove(m);
        m.setParent(null);
    }

    public ObservableList<Moment> momentsProperty() { return submoments; }

    public int indexOf(Moment m) {
        for(int i = 0; i < submoments.size(); i++){
            if(submoments.get(i) == m)
                return i;
        }
        return -1;
    }

    public int numberOfSubMoments() {
        return submoments.size();
    }

}

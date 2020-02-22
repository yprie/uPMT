package components.modelisationSpace.moment.model;

import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.justification.models.Justification;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class Moment extends RootMoment {

    private SimpleStringProperty name;
    private Justification justification;
    private ListProperty<ConcreteCategory> categories;

    public Moment(String name) {
        super();
        this.name = new SimpleStringProperty(name);
        this.justification = new Justification();
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public Moment(String name, Justification j) {
        super();
        this.name = new SimpleStringProperty(name);
        this.justification = j;
        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public Moment(String name, Descripteme d) {
        super();
        this.name = new SimpleStringProperty(name);

        this.justification = new Justification();
        this.justification.addDescripteme(d);

        this.categories = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return this.name.get(); }
    public ObservableValue<String> nameProperty() { return name; }

    public Justification getJustification() { return justification; }


    public void addCategory(ConcreteCategory cc) {
        categories.add(cc);
    }
    public void removeCategory(ConcreteCategory cc) {
        categories.remove(cc);
    }
    public ObservableList<ConcreteCategory> concreteCategoriesProperty() { return categories; }

}

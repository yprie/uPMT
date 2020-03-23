package models;

import components.modelisationSpace.category.appCommands.RemoveConcreteCategoryCommand;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

import java.util.LinkedList;

public class Moment extends RootMoment implements IDraggable {
    public static final DataFormat format = new DataFormat("Moment");

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
        bindListener(cc);
    }
    public void addCategory(int index, ConcreteCategory cc) {
        if(index == categories.size()) {
            addCategory(cc);
        }
        else {
            categories.add(index, cc);
            bindListener(cc);
        }
     }
    public void removeCategory(ConcreteCategory cc) {
        categories.remove(cc);
    }
    public ObservableList<ConcreteCategory> concreteCategoriesProperty() { return categories; }
    public int indexOfConcreteCategory(ConcreteCategory cc) {
        int index = -1;
        for(int i = 0; i < categories.size(); i++)
            if(categories.get(i) == cc)
                return i;
        return index;
    }
    public int indexOfSchemaCategory(SchemaCategory sc) {
        int index = -1;
        for(int i = 0; i < categories.size(); i++)
            if(categories.get(i).isSchemaCategory(sc))
                return i;
        return index;
    }

    private void bindListener(ConcreteCategory category) {
        Moment m = this;
        category.existsProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1){
                    new RemoveConcreteCategoryCommand(m, category, false).execute();
                    System.out.println("removed");
                    category.existsProperty().removeListener(this);
                }
            }
        });
    }

    public boolean hadThisCategory(ConcreteCategory category) {
        boolean had = false;
        for(int i = 0; i < categories.size(); i++) {
            if (category.getSchemaCategory() == categories.get(i).getSchemaCategory()) {
                had = true;
            }
        }
        return had;
    }
    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

}

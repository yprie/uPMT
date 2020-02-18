package components.modelisationSpace.moment.model;

import components.interviewPanel.Models.Descripteme;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Moment extends RootMoment {

    private SimpleStringProperty name;


    public Moment(String name) {
        super();
        this.name = new SimpleStringProperty(name);
    }

    public Moment(String name, Descripteme d) {
        super();
        this.name = new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return this.name.get(); }
    public ObservableValue<String> nameProperty() { return name; }

}

package components.modelisationSpace.moment.model;

import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.models.Justification;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Moment extends RootMoment {

    private SimpleStringProperty name;
    private Justification justification;

    public Moment(String name) {
        super();
        this.name = new SimpleStringProperty(name);
        this.justification = new Justification();
    }

    public Moment(String name, Descripteme d) {
        super();
        this.name = new SimpleStringProperty(name);

        this.justification = new Justification();
        this.justification.addDescripteme(d);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return this.name.get(); }
    public ObservableValue<String> nameProperty() { return name; }

    public Justification getJustification() { return justification; }
}

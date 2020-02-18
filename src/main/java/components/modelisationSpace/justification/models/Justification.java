package components.modelisationSpace.justification.models;

import components.interviewPanel.Models.Descripteme;
import components.schemaTree.Cell.Models.SchemaProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class Justification {

    private SimpleListProperty<Descripteme> descriptemes;

    public Justification() {
        this.descriptemes = new SimpleListProperty<Descripteme>(FXCollections.observableList(new LinkedList<Descripteme>()));
    }

    public void addDescripteme(Descripteme d) {
        this.descriptemes.add(d);
    }
    public void addDescripteme(Descripteme d, int index) {
        //Mainly debug purposes
        //if(index > this.descriptemes.size())
        //    throw new IllegalArgumentException("[Justification] The descripteme couldn't not be added to the justification because of a wrong index : " + index + "/" + descriptemes.size() + " !");
        System.out.println("Attempting Index : " + index);
        if(index == descriptemes.size())
            addDescripteme(d);
        else
            this.descriptemes.add(index, d);
    }
    public void removeDescripteme(Descripteme d) {
        this.descriptemes.remove(d);
    }

    public ObservableList<Descripteme> descriptemesProperty() { return descriptemes; }

    public int indexOf(Descripteme d) {
        return descriptemes.indexOf(d);
    }

}

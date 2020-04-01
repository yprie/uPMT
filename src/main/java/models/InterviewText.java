package models;

import javafx.beans.property.SimpleListProperty;

import java.io.Serializable;

public class InterviewText implements Serializable {

    private String text;
    private SimpleListProperty<Annotation> annotations;

    public InterviewText(String text) {
        this.text = text;
    }

    public String getText() { return text; }
}

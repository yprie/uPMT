package models;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class InterviewText implements Serializable {

    private String text;
    private SimpleListProperty<Annotation> annotations;

    public InterviewText(String text) {
        this.text = text;
        this.annotations = new SimpleListProperty<Annotation>(FXCollections.observableList(new LinkedList<Annotation>()));
    }

    public String getText() { return text; }
    public SimpleListProperty<Annotation> getAnnotationsProperty() { return annotations; }

    public void removeAnnotation(Annotation annotation) {
        annotations.remove(annotation);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    public Annotation getFirstAnnotationByIndex(int index) {
        ArrayList<Annotation> foundAnnotations = new ArrayList<Annotation>();
        for (Annotation annotation : annotations) {
            if (annotation.getStartIndex() < index && index < annotation.getEndIndex()) {
                foundAnnotations.add(annotation);
            }
        }
        if (!foundAnnotations.isEmpty()) {
            return foundAnnotations.get(0);
        }
        else {
            return null;
        }
    }

    public String getWordByIndex(int index) {
        int end = text.indexOf(" ", index);
        int start = text.lastIndexOf(" ", index);
        if (start == -1) {
            start = 0;
        }
        if (end == -1) {
            end = text.length() - 1;
        }
        return text.substring(start, end);
    }
}

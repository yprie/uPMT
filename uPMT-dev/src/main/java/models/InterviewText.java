package models;

import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.*;

public class InterviewText implements Serializable {

    private final String text;
    private final SimpleListProperty<Annotation> annotations;

    // Don't save this on disk (redundancy)
    private final ObservableList<Descripteme> descriptemes = FXCollections.observableArrayList(descripteme ->
            new Observable[] {
                    descripteme.startIndex,
                    descripteme.endIndex,
            });

    public InterviewText(String text) {
        this.text = text;
        this.annotations = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
    }

    public InterviewText(String text, ArrayList<Annotation> annotations) {
        this.text = text;
        this.annotations = new SimpleListProperty<>(FXCollections.observableList(new LinkedList<>()));
        annotations.addAll(annotations);
    }

    public String getText() { return text; }

    public SimpleListProperty<Annotation> getAnnotationsProperty() { return annotations; }
    public ObservableList<Descripteme> getDescriptemesProperty() { return descriptemes; }

    public void removeAnnotation(Annotation annotation) {
        annotations.remove(annotation);
        // there is a listener that remove the highlight
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    public Annotation getAnnotationByIndex(int index) {
        for (Annotation annotation : this.annotations) {
            if (annotation.getStartIndex() <= index && index < annotation.getEndIndex()) {
                return annotation;
            }
        }
        return null;
    }

    public ArrayList<Descripteme> getDescriptemesByIndex(int index) {
        ArrayList<Descripteme> foundDescriptemes = new ArrayList<>();
        for (Descripteme descripteme : this.descriptemes) {
            if (descripteme.getStartIndex() <= index && index < descripteme.getEndIndex()) {
                foundDescriptemes.add(descripteme);
            }
        }
        return foundDescriptemes;
    }

    public void addDescripteme(Descripteme descripteme) {
        descriptemes.add(descripteme);
    }

    public void removeDescripteme(Descripteme descripteme) {
        descriptemes.remove(descripteme);
    }

    public List<Annotation> getSortedAnnotation() {
        return annotations.sorted(Comparator.comparingInt(a -> a.startIndex.get()));
    }

    @Override
    public String toString() {
        return text;
    }
}

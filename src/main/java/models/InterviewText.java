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
    private HashMap<Integer, ArrayList<Descripteme>> indexDescriptemes = new HashMap<>();
    private HashMap<Integer, Annotation> indexAnnotation = new HashMap<>();

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
        for (int i = annotation.getStartIndex() ; i < annotation.getEndIndex() ; i++) {
            indexAnnotation.put(i, annotation);
        }
        annotations.add(annotation);
    }

    public Annotation getAnnotationByIndex(int i) {
        return indexAnnotation.get(i);
    }

    public ArrayList<Descripteme> getDescriptemesByIndex(int i) {
        ArrayList<Descripteme> descriptemes = indexDescriptemes.get(i);
        if (descriptemes == null) {
            descriptemes = new ArrayList<>();
            indexDescriptemes.put(i, descriptemes);
        }
        return descriptemes;
    }

    public void addDescripteme(Descripteme descripteme) {
        // called when initializing rich text area
        for (int i = descripteme.getStartIndex() ; i < descripteme.getEndIndex() ; i++) {
            ArrayList<Descripteme> current = indexDescriptemes.get(i);
            if (current == null) {
                current = new ArrayList<>();
            }
            current.add(descripteme);
            indexDescriptemes.put(i, current);
        }
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

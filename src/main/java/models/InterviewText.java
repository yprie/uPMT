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
        this.annotations = new SimpleListProperty<Annotation>(FXCollections.observableList(new LinkedList<Annotation>()));
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
        cutAnnotation(annotation.startIndex.get(), annotation.endIndex.get());
        // TODO: delete an annotation that is inside the new annotation
    }

    public void cutAnnotation(int start, int end) {
        Annotation annotationToCutBefore = getFirstAnnotationByIndex(start);
        Annotation annotationToCutAfter = getFirstAnnotationByIndex(end);
        if (annotationToCutBefore != null && annotationToCutAfter != null) {
            if (annotationToCutBefore == annotationToCutAfter) {
                Annotation annotationEnd = new Annotation(
                        annotationToCutAfter.interviewText,
                        end,
                        annotationToCutAfter.endIndex.get(),
                        annotationToCutAfter.color);
                this.addAnnotation(annotationEnd);
                annotationToCutBefore.setEndIndex(start);
            }
        }
        else {
            if (annotationToCutBefore != null){
                annotationToCutBefore.setEndIndex(start);
            }
            if (annotationToCutAfter != null) {
                annotationToCutAfter.setStartIndex(end);
            }
        }
    }

    public Annotation getFirstAnnotationByIndex(int index) {
        ArrayList<Annotation> foundAnnotations = new ArrayList<Annotation>();
        for (Annotation annotation : this.annotations) {
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

    public ArrayList<Descripteme> getDescriptemesByIndex(int index) {
        ArrayList<Descripteme> foundDescriptemes = new ArrayList<Descripteme>();
        for (Descripteme descripteme : this.descriptemes) {
            if (descripteme.getStartIndex() <=   index && index < descripteme.getEndIndex()) {
                foundDescriptemes.add(descripteme);
            }
        }
        if (!foundDescriptemes.isEmpty()) {
            return foundDescriptemes;
        }
        else {
            return null;
        }
    }

    public void addDescripteme(Descripteme descripteme) {
        descriptemes.add(descripteme);
        descripteme.getDuplicatedDescriptemeProperty().addListener(value -> {
            addDescripteme(descripteme.getDuplicatedDescriptemeProperty().getValue());
        });
    }

    public List<Annotation> getSortedAnnotation() {
        return annotations.sorted(Comparator.comparingInt(a -> a.startIndex.get()));
    }

    @Override
    public String toString() {
        return text;
    }
}

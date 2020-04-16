package components.interviewPanel.utils;

import javafx.scene.paint.Color;
import models.Annotation;
import models.Fragment;

import java.util.HashMap;

public class LetterMap extends HashMap<Integer, TextStyle> {
    Annotation selectedAnnotation;

    public void becomeDescripteme(Fragment fragment) {
        for (int i = fragment.getStartIndex() ; i < fragment.getEndIndex() ; i++) {
            TextStyle style = this.get(i);
            if (style == null) {
                style = new TextStyle(true);
            }
            else {
                style.becomeDescripteme();
            }
            this.put(i, style);
        }
    }

    public void becomeAnnotation(Fragment fragment, Color color) {
        for (int i = fragment.getStartIndex() ; i < fragment.getEndIndex() ; i++) {
            TextStyle style = this.get(i);
            if (style == null) {
                style = new TextStyle(false, true, color);
            }
            else {
                style.becomeAnnotation(color);
            }
            this.put(i, style);
        }
    }

    public void removeDescripteme(Fragment fragment) {
        for (int i = fragment.getStartIndex() ; i < fragment.getEndIndex() ; i++) {
            TextStyle style = this.get(i);
            if (style == null) {
                style = new TextStyle();
            } else {
                style.removeDescripteme();
            }
            this.put(i, style);
        }
    }

    public void removeAnnotation(Fragment fragment) {
        for (int i = fragment.getStartIndex() ; i < fragment.getEndIndex() ; i++) {
            TextStyle style = this.get(i);
            if (style == null) {
                style = new TextStyle();
            }
            else {
                style.removeAnnotation();
            }
            this.put(i, style);
        }
    }

    public TextStyle getStyleByIndex(int index) {
        return this.get(index);
    }

    public void selectAnnotation(Annotation annotation) {
        if (selectedAnnotation != annotation) {
            for (int i = annotation.getStartIndex() ; i < annotation.getEndIndex() ; i++) {
                TextStyle style = this.get(i);
                style.darker();
                this.put(i, style);
            }
            selectedAnnotation = annotation;
        }
    }

    public void deSelectAnnotation() {
        if (selectedAnnotation != null) {
            for (int i = selectedAnnotation.getStartIndex() ; i < selectedAnnotation.getEndIndex() ; i++) {
                TextStyle style = this.get(i);
                style.brighter();
                this.put(i, style);
            }
            selectedAnnotation = null;
        }
    }

    public Annotation getSelectedAnnotation() {
        return selectedAnnotation;
    }
}

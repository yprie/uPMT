package components.interviewPanel.utils;

import javafx.scene.paint.Color;
import models.Fragment;

import java.util.HashMap;

public class LetterMap extends HashMap<Integer, TextStyle> {

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
}

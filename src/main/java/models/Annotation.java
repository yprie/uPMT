package models;

import components.interviewPanel.utils.TextStyle;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.Color;

public class Annotation extends Fragment {
    public static final DataFormat format = new DataFormat("Annotation");
    Color color;

    public Annotation(InterviewText interviewText, int startIndex, int endIndex, Color c) {
        super(interviewText, startIndex, endIndex);
        color = c;
    }

    public void setStartIndex(int start) {
        if (start > endIndex) {
            throw new IllegalArgumentException("Annotation start index can't be after end index");
        }
        startIndex = start;
    }
    public void setEndIndex(int end) {
        if (end < startIndex) {
            throw new IllegalArgumentException("Annotation end index can't be before start index");
        }
        endIndex = end;
    }

    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Descripteme toDescripteme() {
        return new Descripteme(interviewText, startIndex, endIndex);
    }

    public TextStyle getStyle() {
        return new TextStyle(false, true, color);
    }
}

package models;

import javafx.scene.input.DataFormat;
import javafx.scene.paint.Color;

public class Annotation extends Fragment {
    public static final DataFormat format = new DataFormat("Annotation");
    private final Color color;

    private boolean isSelected;

    public Annotation(InterviewText interviewText, int startIndex, int endIndex, Color c) {
        super(interviewText, startIndex, endIndex);
        color = c;
    }

    public Annotation(Fragment fragment, Color color) {
        super(fragment.interviewText, fragment.getIndexRange());
        this.color = color;
    }

    public Annotation(Annotation annotation) {
        super(annotation.interviewText, annotation.getIndexRange());
        this.color = annotation.color;
    }

    public void setStartIndex(int start) {
        if (start > endIndex.get()) {
            throw new IllegalArgumentException("Annotation start index can't be after end index");
        }
        startIndex.set(start);
    }
    public void setEndIndex(int end) {
        if (end < startIndex.get()) {
            throw new IllegalArgumentException("Annotation end index can't be before start index");
        }
        endIndex.set(end);
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
        return super.toString() + " - " + color.toString();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}

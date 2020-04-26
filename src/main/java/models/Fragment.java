package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.IndexRange;
import javafx.scene.input.DataFormat;
import utils.Emphasable;
import utils.dragAndDrop.IDraggable;

public abstract class Fragment extends Emphasable implements IDraggable {
    public static final DataFormat format = new DataFormat("Fragment");

    protected InterviewText interviewText;
    protected IntegerProperty startIndex, endIndex;

    public Fragment(InterviewText interviewText, int startIndex, int endIndex) {
        this.startIndex = new SimpleIntegerProperty(startIndex);
        this.endIndex = new SimpleIntegerProperty(endIndex);
        this.interviewText = interviewText;
    }

    public int getStartIndex() { return startIndex.getValue(); }
    public IntegerProperty startIndexProperty() { return startIndex; }
    public int getEndIndex() { return endIndex.getValue(); }
    public IntegerProperty endIndexProperty() { return endIndex; }
    public InterviewText getInterviewText() { return interviewText; }

    public String getFragmentText() {
        return interviewText.getText().substring(startIndex.getValue(), endIndex.getValue());
    }

    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public String toString() {
        return "Fragment [" + startIndex.get() + ", " + endIndex.get() + "] " + getFragmentText();
    }

    public IndexRange getIndexRange() {
        return new IndexRange(startIndex.get(), endIndex.get());
    }

    /*
    public ArrayList getWordsIndex() {
        ArrayList<String> results = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            String word = interviewText.getWordByIndex(i);
            if (!results.contains(word)) {
                results.add(word);
            }
        }
        return results;
    }

     */
}

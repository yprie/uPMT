package models;

import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

import java.util.ArrayList;

public class Fragment implements IDraggable {
    public static final DataFormat format = new DataFormat("Fragment");

    protected InterviewText interviewText;
    protected int startIndex, endIndex;

    public Fragment(InterviewText interviewText, int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.interviewText = interviewText;
    }

    public int getStartIndex() { return startIndex; }
    public int getEndIndex() { return endIndex; }
    public InterviewText getInterviewText() { return interviewText; }

    public String getFragmentText() {
        return interviewText.getText().substring(startIndex, endIndex);
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
        return "Fragment [" + startIndex + ", " + endIndex + "] " + getFragmentText();
    }

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
}

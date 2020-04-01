package models;

import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

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
        return "Fragment " + getFragmentText();
    }
}

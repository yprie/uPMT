package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

public class Descripteme implements IDraggable {

    public static final DataFormat format = new DataFormat("Descripteme");
    private InterviewText interviewText;
    private int startIndex, endIndex;
    private SimpleStringProperty descripteme; // this is the selection (getSelection), the substring of the interview text

    public Descripteme(InterviewText interviewText, int startIndex, int endIndex){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.interviewText = interviewText;
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());
    }

    public int getStartIndex() { return startIndex; }
    public int getEndIndex() { return endIndex; }

    public InterviewText getInterviewText() { return interviewText; }

    public final SimpleStringProperty getSelectionProperty() {
        return this.descripteme;
    }

    public final String getSelection() {
        return interviewText.getText().substring(startIndex, endIndex).replace("\n", "").replace("\r", "");
    }

    public Descripteme duplicate() {
        return new Descripteme(interviewText, startIndex, endIndex);
    }

    public void modifyIndex(int start, int end) {
        startIndex = start;
        endIndex = end;
        this.descripteme.set(getSelection());
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
        String result = super.toString();
        result += " - ";
        //result += interviewText.getText();
        result += getSelection();
        return result;
    }
}

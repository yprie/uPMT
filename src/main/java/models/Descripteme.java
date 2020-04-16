package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.DataFormat;

public class Descripteme extends Fragment {

    public static final DataFormat format = new DataFormat("Descripteme");

    private final SimpleStringProperty descripteme; // this is the selection (getSelection), the substring of the interview text

    public Descripteme(InterviewText interviewText, int startIndex, int endIndex){
        super(interviewText, startIndex, endIndex);
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());

    }

    public Descripteme(Annotation a) {
        super(a.getInterviewText(), a.getStartIndex(),a.getEndIndex());
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());
    }

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
        startIndex = start == -1 ? 0 : start;
        endIndex = Math.min(end, interviewText.getText().length());
        this.descripteme.set(getSelection());
    }

    @Override
    public DataFormat getDataFormat() {
        return format;
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

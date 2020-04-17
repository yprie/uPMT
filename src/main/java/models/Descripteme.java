package models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.scene.input.DataFormat;

public class Descripteme extends Fragment {

    // Don't save this on disk, used for linking interview and modeling space (emphasized)
    /*
    On creating a descripteme by Drag and Drop, the InterViewText get the created descripteme
    with the event OnDragDone. When the InterviewText add the reference to this descripteme, it add
    a listener on the duplicatedFormMeDescripteme.
    Then, when this descriptemeis duplicated, the duplicatedFormMeDescripteme change and the InterviewText
    can react to this event by adding the duplicated descripteme to the descripteme list.
    The goal is to be able to know every descripteme in the InterviewText in order to
    emphasis them in the modeling space.
     */
    private SimpleObjectProperty<Descripteme> duplicatedFormMeDescripteme;

    public static final DataFormat format = new DataFormat("Descripteme");

    private final SimpleStringProperty descripteme; // this is the selection (getSelection), the substring of the interview text

    public Descripteme(InterviewText interviewText, int startIndex, int endIndex){
        super(interviewText, startIndex, endIndex);
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());

        duplicatedFormMeDescripteme = new SimpleObjectProperty<>();
    }

    public Descripteme(Annotation a) {
        super(a.getInterviewText(), a.getStartIndex(),a.getEndIndex());
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());

        duplicatedFormMeDescripteme = new SimpleObjectProperty<>();
    }

    public InterviewText getInterviewText() { return interviewText; }

    public final SimpleStringProperty getSelectionProperty() {
        return this.descripteme;
    }

    public final String getSelection() {
        return interviewText.getText().substring(startIndex, endIndex).replace("\n", "").replace("\r", "");
    }

    public Descripteme duplicate() {
        Descripteme newDescripteme = new Descripteme(interviewText, startIndex, endIndex);
        duplicatedFormMeDescripteme.set(newDescripteme);
        return newDescripteme;
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

    public ObservableObjectValue<Descripteme> getDuplicatedDescriptemeProperty() {
        return duplicatedFormMeDescripteme;
    }
}

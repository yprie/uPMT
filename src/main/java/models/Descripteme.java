package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.input.DataFormat;

public class Descripteme extends Fragment {
    public static final DataFormat format = new DataFormat("Descripteme");

    // this is the selection (getSelection), the substring of the interview text
    private final SimpleStringProperty descripteme;

    // Whether or not the descripteme is highlighted is the interview
    private SimpleBooleanProperty isRevealed = new SimpleBooleanProperty(false);

    // When set to true, scroll the interview to the descripteme
    private SimpleBooleanProperty triggerScrollReveal = new SimpleBooleanProperty(false);

    public Descripteme(InterviewText interviewText, int startIndex, int endIndex){
        super(interviewText, startIndex, endIndex);
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());
    }

    public Descripteme(Annotation a) {
        super(a.getInterviewText(), a.getIndexRange());
        descripteme = new SimpleStringProperty();
        descripteme.set(getSelection());
    }

    public InterviewText getInterviewText() { return interviewText; }

    public final SimpleStringProperty getSelectionProperty() {
        return this.descripteme;
    }

    public final String getSelection() {
        return interviewText.getText().substring(startIndex.get(), endIndex.get()).replace("\n", "").replace("\r", "");
    }

    public Descripteme duplicate() {
        return new Descripteme(interviewText, startIndex.get(), endIndex.get());
    }

    public void modifyIndex(int start, int end) {
        startIndex.set(start == -1 ? 0 : start);
        endIndex.set(Math.min(end, interviewText.getText().length()));
        this.descripteme.set(getSelection());
    }

    @Override
    public DataFormat getDataFormat() {
        return format;
    }

    public void setRevealed(boolean revealed) {
        this.isRevealed.set(revealed);
    }

    public SimpleBooleanProperty getRevealedProperty() {
        return isRevealed;
    }

    public void setTriggerScrollReveal(boolean triggerScrollReveal) {
        this.triggerScrollReveal.set(triggerScrollReveal);
    }

    public SimpleBooleanProperty getTriggerScrollReveal() {
        return triggerScrollReveal;
    }
}

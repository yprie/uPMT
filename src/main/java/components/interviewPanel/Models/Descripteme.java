package components.interviewPanel.Models;

import javafx.scene.input.DataFormat;
import utils.dragAndDrop.IDraggable;

public class Descripteme implements IDraggable {

    public static final DataFormat format = new DataFormat("Descripteme");
    private InterviewText interviewText;
    private int startIndex, endIndex;

    public Descripteme(InterviewText interviewText, int startIndex, int endIndex){
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.interviewText = interviewText;
    }

    public final String getSelection() {
        return interviewText.getText().substring(startIndex, endIndex);
    }

    public Descripteme duplicate() {
        return new Descripteme(interviewText, startIndex, endIndex);
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
        result += interviewText.getText();
        return result;
    }
}

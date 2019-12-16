package Components.InterviewPanel.Models;

import java.io.Serializable;

public class InterviewText implements Serializable {

    private String text;

    public InterviewText(String text) {
        this.text = text;
    }

    public String getText() { return text; }
}

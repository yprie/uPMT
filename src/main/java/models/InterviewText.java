package models;

import java.io.Serializable;

public class InterviewText implements Serializable {

    private String text;

    public InterviewText(String text) {
        this.text = text;
    }

    public String getText() { return text; }

    @Override
    public String toString() {
        return text;
    }
}

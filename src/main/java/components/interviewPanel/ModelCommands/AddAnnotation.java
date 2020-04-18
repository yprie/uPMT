package components.interviewPanel.ModelCommands;

import application.history.ModelUserActionCommand;
import models.Annotation;
import models.InterviewText;

public class AddAnnotation extends ModelUserActionCommand<Void, Void> {
    Annotation annotation;
    InterviewText interviewText;

    public AddAnnotation(InterviewText i, Annotation a) {
        interviewText = i;
        annotation = a;
    }

    @Override
    public Void execute() {
        interviewText.addAnnotation(annotation);
        return null;
    }

    @Override
    public Void undo() {
        interviewText.removeAnnotation(annotation);
        return null;
    }
}

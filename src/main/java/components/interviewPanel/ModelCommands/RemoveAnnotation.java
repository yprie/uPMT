package components.interviewPanel.ModelCommands;

import application.history.ModelUserActionCommand;
import models.Annotation;
import models.InterviewText;

public class RemoveAnnotation extends ModelUserActionCommand<Void, Void> {
    Annotation annotation;
    InterviewText interviewText;

    public RemoveAnnotation(InterviewText i, Annotation a) {
        interviewText = i;
        annotation = a;
    }

    @Override
    public Void execute() {
        interviewText.removeAnnotation(annotation);
        return null;
    }

    @Override
    public Void undo() {
        interviewText.addAnnotation(annotation);
        return null;
    }
}

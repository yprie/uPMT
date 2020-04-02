package components.interviewPanel.ModelCommands;

import application.history.ModelUserActionCommand;
import models.Annotation;
import models.InterviewText;

public class addAnnotationCommand extends ModelUserActionCommand<Void, Void> {
    Annotation annotation;
    InterviewText interviewText;

    public addAnnotationCommand(Annotation a, InterviewText i) {
        annotation = a;
        interviewText = i;
    }

    @Override
    public Void undo() {
        interviewText.removeAnnotation(annotation);
        return null;
    }

    @Override
    public Void execute() {
        interviewText.addAnnotation(annotation);
        System.out.println("new annotation in interview text");
        return null;
    }
}

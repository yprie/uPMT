package components.interviewPanel.appCommands;

import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

public class addAnnotationCommand implements Executable<Void> {
    Annotation annotation;
    InterviewText interviewText;

    public addAnnotationCommand(Annotation a, InterviewText i) {
        annotation = a;
        interviewText = i;
    }

    @Override
    public Void execute() {

        return null;
    }
}

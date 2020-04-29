package components.interviewPanel.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.ModelCommands.AddAnnotation;
import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

public class AddAnnotationCommand implements Executable<Void> {
    private final Annotation annotation;
    private final InterviewText interviewText;

    public AddAnnotationCommand(InterviewText i, Annotation a) {
        interviewText = i;
        annotation = a;
    }

    @Override
    public Void execute() {
        Boolean newModelUserActionCommandExecuted = new EraseAnnotationCommand(interviewText, annotation.getIndexRange()).execute();
        HistoryManager.addCommand(new AddAnnotation(interviewText, annotation), !newModelUserActionCommandExecuted);
        return null;
    }
}

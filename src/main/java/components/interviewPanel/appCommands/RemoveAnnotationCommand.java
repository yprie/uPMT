package components.interviewPanel.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.ModelCommands.RemoveAnnotation;
import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

public class RemoveAnnotationCommand implements Executable<Void> {
    private final Annotation annotation;
    private final InterviewText interviewText;
    private final boolean newModelUserActionCommand;

    public RemoveAnnotationCommand(InterviewText i, Annotation a, boolean newModelUserActionCommand) {
        interviewText = i;
        annotation = a;
        this.newModelUserActionCommand = newModelUserActionCommand;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveAnnotation(interviewText, annotation), newModelUserActionCommand);
        return null;
    }
}

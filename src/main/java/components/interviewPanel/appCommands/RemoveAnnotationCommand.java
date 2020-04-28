package components.interviewPanel.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.ModelCommands.RemoveAnnotation;
import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

public class RemoveAnnotationCommand implements Executable<Void> {
    private Annotation annotation;
    private InterviewText interviewText;

    public RemoveAnnotationCommand(InterviewText i, Annotation a) {
        interviewText = i;
        annotation = a;
    }

    @Override
    public Void execute() {
        RemoveAnnotation cmd = new RemoveAnnotation(interviewText, annotation);
        HistoryManager.addCommand(cmd, true); // add cmd in history and execute cmd
        cmd.execute();

        return null;
    }
}

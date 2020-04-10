package components.interviewPanel.appCommands;

import components.interviewPanel.ModelCommands.AddAnnotation;
import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

public class AddAnnotationCommand implements Executable<Void> {
    private Annotation annotation;
    private InterviewText interviewText;

    public AddAnnotationCommand(InterviewText i, Annotation a) {
        interviewText = i;
        annotation = a;
    }

    @Override
    public Void execute() {
        AddAnnotation cmd = new AddAnnotation(interviewText, annotation);

        //HistoryManager.addCommand(cmd, true); // add cmd in history and execute cmd
        cmd.execute();

        return null;
    }
}

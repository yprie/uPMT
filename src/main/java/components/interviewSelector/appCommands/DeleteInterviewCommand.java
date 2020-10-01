package components.interviewSelector.appCommands;

import application.history.HistoryManager;
import models.Project;
import models.Interview;

public class DeleteInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;

    public DeleteInterviewCommand(Project project, Interview interview) {
        super(project);
        this.interview = interview;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new components.interviewSelector.modelCommands.DeleteInterviewCommand(project, interview), true);
        return null;
    }
}

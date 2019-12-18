package components.interviewSelector.appCommands;

import application.history.HistoryManager;
import application.project.models.Project;
import components.interviewSelector.models.Interview;

public class SelectCurrentInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;

    public SelectCurrentInterviewCommand(Project project, Interview i) {
        super(project);
        this.interview = i;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new components.interviewSelector.modelCommands.SelectCurrentInterviewCommand(project, interview), false);
        return null;
    }
}

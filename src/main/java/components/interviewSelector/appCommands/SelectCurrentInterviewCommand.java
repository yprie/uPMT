package components.interviewSelector.appCommands;

import application.history.HistoryManager;
import application.project.models.Project;
import components.interviewSelector.models.Interview;

public class SelectCurrentInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;
    private boolean addToCommandHistory;

    public SelectCurrentInterviewCommand(Project project, Interview i, boolean addToCommandHistory) {
        super(project);
        this.interview = i;
        this.addToCommandHistory = addToCommandHistory;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new components.interviewSelector.modelCommands.SelectCurrentInterviewCommand(project, interview), addToCommandHistory);
        return null;
    }
}

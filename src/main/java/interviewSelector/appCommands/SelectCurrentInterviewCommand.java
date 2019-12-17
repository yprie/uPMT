package interviewSelector.appCommands;

import application.History.HistoryManager;
import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class SelectCurrentInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;

    public SelectCurrentInterviewCommand(Project project, Interview i) {
        super(project);
        this.interview = i;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new interviewSelector.modelCommands.SelectCurrentInterviewCommand(project, interview), false);
        return null;
    }
}

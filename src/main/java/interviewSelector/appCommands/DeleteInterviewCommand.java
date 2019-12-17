package interviewSelector.appCommands;

import application.History.HistoryManager;
import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class DeleteInterviewCommand extends InterviewSelectorCommand<Void> {

    private Interview interview;

    public DeleteInterviewCommand(Project project, Interview interview) {
        super(project);
        this.interview = interview;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new interviewSelector.modelCommands.DeleteInterviewCommand(project, interview), true);
        return null;
    }
}

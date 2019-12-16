package interviewSelector.commands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class AddInterviewCommand extends InterviewSelectorCommand<Void, Void> {

    private Interview interview;

    public AddInterviewCommand(Project p, Interview i) {
        super(p);
        this.interview = i;
    }

    @Override
    public Void execute() {
        project.addInterview(interview);
        return null;
    }

    @Override
    public Void undo() {
        project.removeInterview(interview);
        return null;
    }
}

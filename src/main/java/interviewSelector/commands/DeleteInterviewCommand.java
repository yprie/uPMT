package interviewSelector.commands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;


public class DeleteInterviewCommand extends InterviewSelectorCommand<Void, Void> {

    private Interview interview;

    public DeleteInterviewCommand(Project p, Interview i) {
        super(p);
        this.interview = i;
    }

    @Override
    public Void execute() {
        project.removeInterview(interview);
        return null;
    }

    @Override
    public Void undo() {
        project.addInterview(interview);
        return null;
    }
}
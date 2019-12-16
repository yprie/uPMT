package interviewSelector.commands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class AddInterviewCommand extends InterviewSelectorCommand<Void, Void> {

    private Interview interview;
    private Interview previousSelectedInterview;

    public AddInterviewCommand(Project p, Interview i) {
        super(p);
        this.interview = i;
        this.previousSelectedInterview = p.getSelectedInterview();
    }

    @Override
    public Void execute() {
        project.addInterview(interview);
        return null;
    }

    @Override
    public Void undo() {
        project.removeInterview(interview);
        if(previousSelectedInterview != null)
            new SelectCurrentInterviewCommand(project, previousSelectedInterview).execute();
        return null;
    }
}

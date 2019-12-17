package interviewSelector.appCommands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class InterviewSelectorCommandFactory {

    private Project project;
    public InterviewSelectorCommandFactory(Project project) { this.project = project; }

    public CreateNewInterviewCommand createNewInterview() { return new CreateNewInterviewCommand(project); }
    public SelectCurrentInterviewCommand selectCurrentInterview(Interview interview) { return new SelectCurrentInterviewCommand(project, interview); }
    public DeleteInterviewCommand deleteInterview(Interview interview) { return new DeleteInterviewCommand(project, interview); }

}

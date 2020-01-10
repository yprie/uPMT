package components.interviewSelector.appCommands;

import application.project.models.Project;
import components.interviewSelector.models.Interview;

public class InterviewSelectorCommandFactory {

    private Project project;
    public InterviewSelectorCommandFactory(Project project) { this.project = project; }

    public InterviewSelectorCommand<Void> createNewInterview() { return new CreateNewInterviewCommand(project); }
    public InterviewSelectorCommand<Void> selectCurrentInterview(Interview interview) { return new SelectCurrentInterviewCommand(project, interview); }
    public InterviewSelectorCommand<Void> deleteInterview(Interview interview) { return new DeleteInterviewCommand(project, interview); }

}

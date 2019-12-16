package interviewSelector.commands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;

public class InterviewSelectorCommandFactory {

    private Project project;
    public InterviewSelectorCommandFactory(Project project) { this.project = project; }

    public AddInterviewCommand addInterview(Interview interview) { return new AddInterviewCommand(project, interview); }
    public DeleteInterviewCommand removeInterview(Interview interview) { return new DeleteInterviewCommand(project, interview); }
    public SelectCurrentInterviewCommand selectInterview(Interview interview) { return new SelectCurrentInterviewCommand(project, interview); }
}

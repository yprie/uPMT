package components.interviewSelector.appCommands;

import models.Project;
import models.Interview;

public class InterviewSelectorCommandFactory {

    private Project project;
    public InterviewSelectorCommandFactory(Project project) { this.project = project; }

    public InterviewSelectorCommand<Void> createNewInterview() { return new CreateNewInterviewCommand(project); }
    public InterviewSelectorCommand<Void> selectCurrentInterview(Interview interview, boolean addToCommandHistory) { return new SelectCurrentInterviewCommand(project, interview, addToCommandHistory); }
    public InterviewSelectorCommand<Void> deleteInterview(Interview interview) { return new DeleteInterviewCommand(project, interview); }
    public InterviewSelectorCommand<Void> modifyInterview(Interview interview) { return new ModifyInterviewCommand(project, interview); }

}

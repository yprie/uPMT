package components.interviewSelector.appCommands;

import components.comparison.controllers.ComparisonSelectionInterviewsControler;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import models.Interview;
import models.Project;

public class InterviewSelectorCommandFactory {

    private Project project;
    public InterviewSelectorCommandFactory(Project project) { this.project = project; }

    public InterviewSelectorCommand<Void> createNewInterview() { return new CreateNewInterviewCommand(project); }
    public InterviewSelectorCommand<Void> selectCurrentInterview(Interview interview, boolean addToCommandHistory) { return new SelectCurrentInterviewCommand(project, interview, addToCommandHistory); }
    public InterviewSelectorCommand<Void> deleteInterview(Interview interview) { return new DeleteInterviewCommand(project, interview); }
    public InterviewSelectorCommand<Void> modifyInterview(Interview interview) { return new ModifyInterviewCommand(project, interview); }
    public ComparisonSelectionInterviewsControler createComparison() { return new ComparisonSelectionInterviewsControler(); }
    public ChangeColorInterviewCommand colorCommand(Interview interview, String color, InterviewSelectorCellController interviewSelectorCellController) {
        return new ChangeColorInterviewCommand(interview, color, interviewSelectorCellController);
    }
}

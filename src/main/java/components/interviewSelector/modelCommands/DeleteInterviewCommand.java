package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import models.Project;
import models.Interview;


public class DeleteInterviewCommand extends ModelUserActionCommand<Void, Void> {

    private Project project;
    private Interview interview;
    private Interview previousInterview;

    public DeleteInterviewCommand(Project p, Interview i) {
        this.project = p;
        this.previousInterview = project.getSelectedInterview();
        this.interview = i;
    }

    @Override
    public Void execute() {
        project.removeInterview(interview);
        if(previousInterview == interview)
            new SelectCurrentInterviewCommand(project, project.interviewsProperty().size() > 0 ? project.interviewsProperty().get(0) : null).execute();
        return null;
    }

    @Override
    public Void undo() {
        project.addInterview(interview);
        if(previousInterview == interview)
            new SelectCurrentInterviewCommand(project, interview).execute();
        return null;
    }
}
package components.interviewSelector.modelCommands;

import application.history.ModelUserActionCommand;
import models.Interview;
import models.Project;

public class PrintAsInterviewCommand extends ModelUserActionCommand<Void, Void> {

    private Project project;
    private Interview interview;
    private Interview previousSelectedInterview;

    public PrintAsInterviewCommand(Project p, Interview i) {
        this.project = p;
        this.interview = i;
        this.previousSelectedInterview = p.getSelectedInterview();
    }

    @Override
    public Void execute() {
        if(this.previousSelectedInterview == this.interview){
            System.out.println("Print l'image dans le bon dossier");
        }

        return null;
    }

    @Override
    public Void undo() {
        project.removeInterview(interview);
        return null;
    }
}
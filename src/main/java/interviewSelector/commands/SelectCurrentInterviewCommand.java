package interviewSelector.commands;

import application.Project.Models.Project;
import interviewSelector.Models.Interview;
import utils.Command.Executable;

public class SelectCurrentInterviewCommand implements Executable<Void> {

    private Project project;
    private Interview interview;

    public SelectCurrentInterviewCommand(Project project, Interview i) {
        this.project = project;
        this.interview = i;
    }

    @Override
    public Void execute() {
        project.setSelectedInterview(interview);
        return null;
    }
}

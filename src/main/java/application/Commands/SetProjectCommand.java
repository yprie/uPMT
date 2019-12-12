package application.Commands;

import application.History.HistoryManager;
import application.Project.Models.Project;
import application.Configuration.Configuration;
import application.UPMTApp;

import java.io.IOException;

public class SetProjectCommand extends ApplicationCommand<Void> {

    private Project project;
    private String projectPath;

    public SetProjectCommand(UPMTApp application, Project project, String projectPath) {
        super(application);
        this.project = project;
        this.projectPath = projectPath;
    }

    @Override
    public Void execute() {
        new ChangeApplicationTitleCommand(upmtApp, project.getName()).execute();
        try{
            if(projectPath != null)
                Configuration.addToProjects(projectPath);
            upmtApp.setCurrentProject(project, projectPath);
            HistoryManager.clearActionStack();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

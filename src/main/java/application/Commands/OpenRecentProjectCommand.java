package application.Commands;

import Project.Controllers.ProjectDialogBox;
import Project.Models.Project;
import Project.Persistency.ProjectLoader;
import application.UPMTApp;

public class OpenRecentProjectCommand extends ApplicationCommand<Void> {

    private String path;

    public OpenRecentProjectCommand(UPMTApp application, String path) {
        super(application);
        this.path = path;
    }

    @Override
    public Void execute() {
        try {
            Project project = ProjectLoader.load(path);
            new SetProjectCommand(upmtApp, project, path).execute();
        } catch (Exception e) {
            e.printStackTrace();
            ProjectDialogBox.projectLoadingFailed();
        }
        return null;
    }
}

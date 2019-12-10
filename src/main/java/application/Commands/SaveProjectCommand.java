package application.Commands;

import application.Project.Controllers.ProjectDialogBox;
import Persistency.ProjectSaver;
import application.Configuration.Configuration;
import application.UPMTApp;

public class SaveProjectCommand extends ApplicationCommand<Void> {


    public SaveProjectCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        //check if there is a current project.
        if(upmtApp.getCurrentProjectPath() != null) {
            try {
                ProjectSaver.save(upmtApp.getCurrentProject(), Configuration.getProjectsPath()[0]);
            } catch (Exception e) {
                ProjectDialogBox.projectSavingFailed();
                e.printStackTrace();
            }
        }
        else {
            new SaveProjectAsCommand(upmtApp).execute();
        }
        return null;
    }
}

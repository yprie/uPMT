package application.appCommands;

import application.history.HistoryManager;
import application.project.controllers.ProjectDialogBox;
import persistency.ProjectSaver;
import application.configuration.Configuration;
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
                upmtApp.setLastSavedCommandId(HistoryManager.getCurrentCommandId());
                new ProjectSavingStatusChangedCommand(upmtApp).execute();

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

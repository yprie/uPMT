package application.appCommands;

import Persistency.ProjectSaver;
import application.History.HistoryManager;
import application.Project.Controllers.SaveAsProjectController;
import application.Configuration.Configuration;
import application.UPMTApp;

import java.io.IOException;

public class SaveProjectAsCommand extends ApplicationCommand<Void> {

    public SaveProjectAsCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        SaveAsProjectController controller = SaveAsProjectController.createSaveAsProjectController(upmtApp.getPrimaryStage(), upmtApp.getCurrentProject());
        if(controller.getState() == SaveAsProjectController.State.SUCCESS) {
            try {
                ProjectSaver.save(upmtApp.getCurrentProject(), controller.getSavePath());
                upmtApp.setLastSavedCommandId(HistoryManager.getCurrentCommandId());
                new ProjectSavingStatusChangedCommand(upmtApp).execute();
                Configuration.addToProjects(controller.getSavePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

package application.Commands;

import Project.Controllers.SaveAsProjectController;
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
                Configuration.addToProjects(controller.getSavePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

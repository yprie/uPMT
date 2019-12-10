package application.Commands;

import application.Project.Controllers.OpenProjectController;
import application.UPMTApp;

public class OpenProjectFromFileCommand extends ApplicationCommand<OpenProjectController> {

    public OpenProjectFromFileCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public OpenProjectController execute() {
        OpenProjectController controller = OpenProjectController.createOpenProjectController(upmtApp.getPrimaryStage());
        if(controller.getState() == OpenProjectController.State.SUCCESS) {
            new SetProjectCommand(upmtApp, controller.getResultProject(), controller.getProjectPath()).execute();
        }
        return controller;
    }
}

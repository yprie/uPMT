package application.Commands;

import Project.Controllers.NewProjectController;
import application.UPMTApp;

public class NewProjectCommand extends ApplicationCommand<Void> {

    public NewProjectCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        NewProjectController controller = NewProjectController.createNewProject();
        if(controller.getState() == NewProjectController.State.SUCCESS) {
            new SetProjectCommand(upmtApp, controller.getCreatedProject(), null).execute();
        }
        return null;
    }
}

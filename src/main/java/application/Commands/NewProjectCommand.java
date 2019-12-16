package application.Commands;

import application.Project.Controllers.NewProjectController;
import application.UPMTApp;
import utils.DialogState;

public class NewProjectCommand extends ApplicationCommand<Void> {

    public NewProjectCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        NewProjectController controller = NewProjectController.createNewProject();
        if(controller.getState() == DialogState.SUCCESS) {
            new SetProjectCommand(upmtApp, controller.getCreatedProject(), null).execute();
        }
        return null;
    }
}

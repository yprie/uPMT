package application.appCommands;

import application.project.controllers.ProjectSelectionController;
import application.UPMTApp;
import components.interviewSelector.appCommands.CreateNewInterviewCommand;

public class OpenProjectManagerCommand extends ApplicationCommand<Void> {

    public OpenProjectManagerCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        ProjectSelectionController controller = ProjectSelectionController.openProjectSelection();
        if(controller.getState() == ProjectSelectionController.State.SUCCESS){
            new SetProjectCommand(upmtApp, controller.getChoosenProject(), null).execute();
            //If we are on a fresh new project
            if(controller.getChoosenProjectPath() == null)
                new CreateNewInterviewCommand(upmtApp.getCurrentProject()).execute();
        }
        return null;
    }
}

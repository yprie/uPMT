package application.Commands;

import Components.InterviewSelector.Controllers.NewInterviewController;
import application.UPMTApp;

public class NewInterviewCommand extends ApplicationCommand<Void> {

    public NewInterviewCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        NewInterviewController controller = NewInterviewController.createNewInterview();
        return null;
    }
}

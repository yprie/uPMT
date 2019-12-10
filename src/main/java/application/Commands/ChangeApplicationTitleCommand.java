package application.Commands;

import application.UPMTApp;

public class ChangeApplicationTitleCommand extends ApplicationCommand<Void> {

    private String newTitle;

    public ChangeApplicationTitleCommand(UPMTApp upmtApp, String newTitle) {
        super(upmtApp);
        this.newTitle = newTitle;
    }

    @Override
    public Void execute() {
        upmtApp.getPrimaryStage().setTitle(newTitle);
        return null;
    }
}

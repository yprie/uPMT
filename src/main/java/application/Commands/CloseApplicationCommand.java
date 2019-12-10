package application.Commands;

import application.UPMTApp;

public class CloseApplicationCommand extends ApplicationCommand<Void> {

    public CloseApplicationCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        //TODO check for unsaved work
        System.exit(0);
        return null;
    }
}

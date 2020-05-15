package application.appCommands;

import application.UPMTApp;
import utils.command.Executable;

public abstract class ApplicationCommand<ExecuteResult> implements Executable<ExecuteResult> {

    protected UPMTApp upmtApp;

    public ApplicationCommand(UPMTApp application) {
        this.upmtApp = application;
    }

}

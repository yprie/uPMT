package application.appCommands;

import application.UPMTApp;

public class ExportAsPngCommand extends ApplicationCommand<Void>{

    UPMTApp application;

    public ExportAsPngCommand(UPMTApp application){
        super(application);
        this.application = application;
    }

    @Override
    public Void execute(){
        System.out.println("Test");
        return null;
    }
}

package application.appCommands;

import application.UPMTApp;
import models.Moment;
import models.RootMoment;
import utils.GlobalVariables;

public class CollapseAllMoments extends ApplicationCommand<Void> {

    public CollapseAllMoments(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        RootMoment rootMoment = GlobalVariables.getRootMoment();
        for (Moment subMoment: rootMoment.momentsProperty()) {
            subMoment.setCollapsed(true);
            iterateOverSubMoment(subMoment);
        }
        return null;
    }

    private void iterateOverSubMoment(Moment moment) {
        for (Moment subMoment: moment.momentsProperty()) {
            subMoment.setCollapsed(true);
            iterateOverSubMoment(subMoment);
        }
    }
}

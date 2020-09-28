package application.appCommands;

import application.UPMTApp;
import models.Moment;
import models.RootMoment;
import utils.GlobalVariables;

public class CollapseAllMoments extends ApplicationCommand<Void> {

    private boolean collapse;

    public CollapseAllMoments(UPMTApp application, boolean collapse) {
        super(application);
        this.collapse = collapse;
    }

    @Override
    public Void execute() {
        RootMoment rootMoment = GlobalVariables.getRootMoment();
        for (Moment subMoment: rootMoment.momentsProperty()) {
            subMoment.setCollapsed(collapse);
            iterateOverSubMoment(subMoment);
        }
        return null;
    }

    private void iterateOverSubMoment(Moment moment) {
        for (Moment subMoment: moment.momentsProperty()) {
            subMoment.setCollapsed(collapse);
            iterateOverSubMoment(subMoment);
        }
    }
}

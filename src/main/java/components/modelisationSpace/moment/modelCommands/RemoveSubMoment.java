package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;
import models.RootMoment;

public class RemoveSubMoment extends ModelUserActionCommand {

    private RootMoment parent;
    private Moment moment;
    private int oldIndex ;


    public RemoveSubMoment(RootMoment parent, Moment moment) {
        this.parent = parent;
        this.moment = moment;
    }

    @Override
    public Void execute() {
        oldIndex = parent.indexOf(moment);
        parent.removeMoment(moment);
        moment.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().removeDescripteme(descripteme);
        });
        moment.momentsProperty().forEach(subMoment -> {
            subMoment.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().removeDescripteme(descripteme);
            });
        });
        return null;
    }

    @Override
    public Void undo() {
        parent.addMoment(oldIndex, moment);
        moment.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().addDescripteme(descripteme);
        });
        moment.momentsProperty().forEach(subMoment -> {
            subMoment.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().addDescripteme(descripteme);
            });
        });
        return null;
    }
}

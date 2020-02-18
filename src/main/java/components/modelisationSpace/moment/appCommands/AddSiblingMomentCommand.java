package components.modelisationSpace.moment.appCommands;


import application.history.HistoryManager;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import components.modelisationSpace.moment.modelCommands.AddSubMoment;
import utils.command.Executable;

public class AddSiblingMomentCommand implements Executable<Void> {

    private RootMoment parent;
    private Moment newMoment;
    int index = -1;

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment, int index) {
        this.parent = parent;
        this.newMoment = newMoment;
        this.index = index;
    }

    public AddSiblingMomentCommand(RootMoment parent, Moment newMoment) {
        this.parent = parent;
        this.newMoment = newMoment;
    }

    @Override
    public Void execute() {
        AddSubMoment cmd;
        if(index == -1)
            cmd = new AddSubMoment(parent, newMoment);
        else
            cmd = new AddSubMoment(parent, newMoment, index);
        HistoryManager.addCommand(cmd, true);
        return null;
    }
}

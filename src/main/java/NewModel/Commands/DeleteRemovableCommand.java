package NewModel.Commands;

import ApplicationHistory.ModelUserActionCommand;
import NewModel.IRemovable;

public class DeleteRemovableCommand extends ModelUserActionCommand<Void, Void> {

    private IRemovable removable;

    public DeleteRemovableCommand(IRemovable removable) {
        this.removable = removable;
    }

    @Override
    public Void execute() {
        removable.existsProperty().setValue(false);
        System.out.println("Deleted !");
        return null;
    }

    @Override
    public Void undo() {
        removable.existsProperty().setValue(true);
        return null;
    }
}

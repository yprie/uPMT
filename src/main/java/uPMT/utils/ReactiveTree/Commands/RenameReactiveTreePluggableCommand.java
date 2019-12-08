package utils.ReactiveTree.Commands;

import ApplicationHistory.ICommand;
import utils.ReactiveTree.ReactiveTreePluggable;

public class RenameReactiveTreePluggableCommand extends ICommand<Void, Void> {

    private ReactiveTreePluggable element;
    private String temp;

    public RenameReactiveTreePluggableCommand(ReactiveTreePluggable element, String newName) {
        this.element = element;
        this.temp = newName;
    }

    @Override
    public Void execute() {
        String t = element.nameProperty().get();
        element.nameProperty().set(temp);
        temp = t;
        return null;
    }

    @Override
    public Void unexecute() {
        String t = element.nameProperty().get();
        element.nameProperty().set(temp);
        temp = t;
        return null;
    }

}

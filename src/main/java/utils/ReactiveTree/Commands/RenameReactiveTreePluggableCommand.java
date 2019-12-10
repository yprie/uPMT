package utils.ReactiveTree.Commands;

import application.History.ModelUserActionCommand;
import utils.ReactiveTree.ReactiveTreePluggable;

public class RenameReactiveTreePluggableCommand extends ModelUserActionCommand<Void, Void> {

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
    public Void undo() {
        String t = element.nameProperty().get();
        element.nameProperty().set(temp);
        temp = t;
        return null;
    }

}

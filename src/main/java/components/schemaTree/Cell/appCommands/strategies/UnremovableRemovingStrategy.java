package components.schemaTree.Cell.appCommands.strategies;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.RemovingStrategy;
import javafx.scene.control.TreeView;
import utils.removable.IRemovable;


public class UnremovableRemovingStrategy<E extends SchemaTreePluggable & IRemovable> extends RemovingStrategy {


    public UnremovableRemovingStrategy() {
        super(null, null);
    }

    @Override
    public Void execute() {
        return null;
    }
}
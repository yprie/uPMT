package components.schemaTree.Cell.appCommands.strategies;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.AddChildStrategy;
import javafx.scene.control.TreeItem;


public class LeafCreateChildStrategy extends AddChildStrategy {

    private SchemaTreePluggable newModel;
    private TreeItem<SchemaTreePluggable> item;

    public LeafCreateChildStrategy() {
        super(null, null, null);
    }

    @Override
    public Void execute() {
        return null;
    }
}
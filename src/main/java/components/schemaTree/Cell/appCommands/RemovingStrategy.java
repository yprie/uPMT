package components.schemaTree.Cell.appCommands;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.SchemaTreeCommand;
import javafx.scene.control.TreeView;

public abstract class RemovingStrategy extends SchemaTreeCommand<Void> {

    protected SchemaTreePluggable modelToRemove;

    public RemovingStrategy(TreeView<SchemaTreePluggable> view, SchemaTreePluggable modelToRemove) {
        super(view);
        this.modelToRemove = modelToRemove;
    }
}

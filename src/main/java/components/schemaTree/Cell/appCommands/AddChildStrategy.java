package components.schemaTree.Cell.appCommands;

import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.appCommands.SchemaTreeCommand;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public abstract class AddChildStrategy extends SchemaTreeCommand<Void> {

    protected TreeItem<SchemaTreePluggable> item;
    protected SchemaTreePluggable newModel;

    public AddChildStrategy(TreeView<SchemaTreePluggable> view, TreeItem<SchemaTreePluggable> item, SchemaTreePluggable newModel) {
        super(view);
        this.item = item;
        this.newModel = newModel;
    }
}

package components.schemaTree.Cell.appCommands;

import components.schemaTree.Cell.SchemaTreePluggable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.command.Executable;

public abstract class SchemaTreeCommand<E> implements Executable<E> {

    protected TreeView<SchemaTreePluggable> view;

    public SchemaTreeCommand(TreeView<SchemaTreePluggable> view) {
        this.view = view;
    }

}

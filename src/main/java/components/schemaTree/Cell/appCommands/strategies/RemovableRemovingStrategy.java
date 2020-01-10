package components.schemaTree.Cell.appCommands.strategies;

import application.history.HistoryManager;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.appCommands.RemovingStrategy;
import components.schemaTree.Cell.modelCommands.RemoveSchemaTreePluggable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.removable.IRemovable;

public class RemovableRemovingStrategy<E extends SchemaTreePluggable&IRemovable> extends RemovingStrategy {

    private E item;
    private SchemaTreePluggable parent;

    public RemovableRemovingStrategy(TreeView<SchemaTreePluggable> view, SchemaTreePluggable parent, E item) {
        super(view, item);
        this.parent = parent;
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new RemoveSchemaTreePluggable<>(parent, item), true);
        TreeItem<SchemaTreePluggable> parentItem = Utils.findTreeElement(view.getRoot(), parent);
        view.getSelectionModel().select(view.getRow(parentItem));
        return null;
    }
}

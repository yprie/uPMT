package components.schemaTree.Cell.appCommands.strategies;

import application.history.HistoryManager;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Utils;
import components.schemaTree.Cell.appCommands.AddChildStrategy;
import components.schemaTree.Cell.modelCommands.AddSchemaTreePluggable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ContainerCreateChildStrategy extends AddChildStrategy {

    public ContainerCreateChildStrategy(TreeView<SchemaTreePluggable> view, TreeItem<SchemaTreePluggable> item, SchemaTreePluggable newModel) {
        super(view, item, newModel);
    }

    @Override
    public Void execute() {
        HistoryManager.addCommand(new AddSchemaTreePluggable(item.getValue(), newModel, true), true);
        item.getValue().expandedProperty().set(true);
        TreeItem<SchemaTreePluggable> i = Utils.findTreeElement(item, newModel);
        int row = view.getRow(i);
        view.scrollTo(row);
        view.getSelectionModel().select(row);
        return null;
    }
}

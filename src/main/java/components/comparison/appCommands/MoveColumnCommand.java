package components.comparison.appCommands;

import application.history.ModelUserActionCommand;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class MoveColumnCommand extends ModelUserActionCommand {
    private final TableView<List<StringProperty>> tableView;
    private final List<TableColumn<List<StringProperty>, ?>> previousState;
    private final int fromIndex;
    private final int toIndex;

    public MoveColumnCommand(TableView<List<StringProperty>> tableView, int fromIndex, int toIndex) {
        this.tableView = tableView;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.previousState = List.copyOf(tableView.getColumns());
    }

    @Override
    public Object execute() {
        // Perform the column reordering operation
        TableColumn<List<StringProperty>, ?> column = tableView.getColumns().remove(fromIndex);
        tableView.getColumns().add(toIndex, column);

        return null;
    }

    @Override
    public Object undo() {
        // Undo the column reordering operation
        tableView.getColumns().setAll(previousState);

        return null;
    }
}
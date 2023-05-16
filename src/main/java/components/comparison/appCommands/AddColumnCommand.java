package components.comparison.appCommands;

import application.history.ModelUserActionCommand;
import components.comparison.controllers.ComparisonTableController;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.command.Executable;

import java.util.List;

public class AddColumnCommand extends ModelUserActionCommand {
    private final int idx;
    private final TableView<List<StringProperty>> tv;
    private final List<TableView<List<StringProperty>>> tables;
    public AddColumnCommand(int columnIndex, TableView<List<StringProperty>> tv, List<TableView<List<StringProperty>>> tables) {
        this.idx = columnIndex;
        this.tv = tv;
        this.tables = tables;
    }

    @Override
    public Void execute() {
        TableColumn<List<StringProperty>, StringProperty> tc = new TableColumn<>("                  ");
        tv.getColumns().add(idx, tc);
        tv.getColumns().get(idx).setSortable(false);
        for (TableView<List<StringProperty>> tableView : tables){
            if (tableView != tv){
                TableColumn<List<StringProperty>, StringProperty> column = new TableColumn<>("                  ");
                tableView.getColumns().add(column);
                tv.getColumns().get(tv.getColumns().size() - 1).setSortable(false);
            }
        }
        return null;
    }

    @Override
    public Object undo() {
        tv.getColumns().remove(idx);
        for (TableView<List<StringProperty>> tableView : tables){
            if (tableView != tv){
                tableView.getColumns().remove(tableView.getColumns().size() - 1);
            }
        }
        return null;
    }

}

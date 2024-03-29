package components.comparison.appCommands;

import application.history.ModelUserActionCommand;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import utils.command.Executable;

import java.util.ArrayList;
import java.util.List;

public class DelColumnCommand extends ModelUserActionCommand {
    private final int idx;
    private final TableView<List<StringProperty>> tv;
    private final List<TableView<List<StringProperty>>>  tables;
    private final List<TableColumn<List<StringProperty>, ?>> previousState;

    public DelColumnCommand(int columnIndex, TableView<List<StringProperty>> tv, List<TableView<List<StringProperty>>> tables) {
        this.idx = columnIndex;
        this.tv = tv;
        this.tables = tables;
        this.previousState = new ArrayList<>(tv.getColumns());
    }
    @Override
    public Void execute() {
        if (tv.getColumns().get(idx).getText().equals("                  ")) {
            tv.getColumns().remove(idx);
            List<Boolean> easy_balance = new ArrayList<>();
            //check if the last column of other tables is empty
            //if so,
            for (TableView<?> tableView : tables) {
                if (tableView != tv) {
                    if (tableView.getColumns().get(tableView.getColumns().size() - 1).getText().equals("                  ")) {
                        easy_balance.add(false);
                    } else {
                        easy_balance.add(true);
                    }
                }
            }
            if (easy_balance.contains(true)) {
                tv.getColumns().add(new TableColumn<>("                  "));
            } else {
                for (TableView<?> tableView : tables) {
                    if (tableView != tv) {
                        tableView.getColumns().remove(tableView.getColumns().size() - 1);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object undo() {
        // Restaurer l'état précédent de la table en réinsérant les colonnes précédentes
        tv.getColumns().setAll(previousState);
        return null;
    }
}

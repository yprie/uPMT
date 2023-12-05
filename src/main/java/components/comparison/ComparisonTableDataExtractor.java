package components.comparison;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ComparisonTableDataExtractor {
    public List<List<List<String>>> extractContainerData(VBox container) {
        List<List<List<String>>> containerData = new ArrayList<>();

        // Iterate over the TableView instances in the VBox container
        for (Node tableView : container.getChildren()) {
            //check if the node is a TableView
            if (tableView.getClass() == TableView.class) {
                List<List<String>> tableData = extractTableData((TableView<?>) tableView);
                containerData.add(tableData);
            }
        }

        return containerData;
    }

    private List<List<String>> extractTableData(TableView<?> tableView) {
        List<List<String>> tableData = new ArrayList<>();

        // Extract column headers
        List<String> columnHeaders = new ArrayList<>();
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            columnHeaders.add(column.getText());
        }
        tableData.add(columnHeaders);


        // Iterate over the rows in the table
        for (int row = 0; row < tableView.getItems().size(); row++) {
            List<String> rowData = new ArrayList<>();

            // Iterate over the columns in each row
            for (TableColumn<?, ?> column : tableView.getColumns()) {
                Object cellValue = column.getCellData(row);
                String cellData = cellValue != null ? cellValue.toString() : "";
                rowData.add(cellData);
            }

            tableData.add(rowData);
        }

        return tableData;
    }
}

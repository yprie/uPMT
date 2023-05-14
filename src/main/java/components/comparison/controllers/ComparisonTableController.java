package components.comparison.controllers;

import application.configuration.Configuration;
import components.comparison.ComparisonTable;
import components.comparison.ComparisonView;
import components.comparison.block;
import components.comparison.line;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistency.newSaveSystem.SConcreteCategory;
import persistency.newSaveSystem.SMoment;

//import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComparisonTableController implements Initializable {

    @FXML
    private VBox table;
    private ObservableList<String> selectionInterviews;

    private ArrayList<Double> columnsWidth = new ArrayList<>();

    public ComparisonTableController(ObservableList<String> selectionInterviews) {
        this.table = new VBox();
        this.selectionInterviews = selectionInterviews;
        initialize(null, null);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable(this.selectionInterviews);
        Platform.runLater(this::bindScroll);
        Platform.runLater(this::setColumnsSizesToBiggest);
        Platform.runLater(this::updateColumnsSizes);

    }


    ///////////////////////////// CREATE TABLE /////////////////////////////
    //remplit la table avec les données
    public void fillTable(ObservableList<String> selectionInterviews) {
        try {
            ComparisonTable ct = new ComparisonTable(selectionInterviews);

            //create table for each interview, we will fill them then
            for (block b : ct.getBlocks()){
                TableView<List<StringProperty>> tv = new TableView<>();
                tv.setPrefWidth(1000);
                tv.setId(b.getTitle());
                addColumn(tv); //allow to add empty columns
                hideScroll(tv);

                this.table.getChildren().add(tv);

                //create the columns for the moments
                createColumns(b, tv);

                //fill the table with the data
                fillLines(b, tv);

                //add empty columns at the end if it's shorter than the bigger interview
                addEmptyColumns(tv, ct.getMaxTableLength());

                int numRows = tv.getItems().size();
                tv.setFixedCellSize(24);
                double rowHeight = tv.getFixedCellSize();
                double tableHeight = (numRows + 1) * rowHeight + 15; // add 1 to account for the header row
                tv.setPrefHeight(tableHeight);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //crée les colonnes pour les moments
    public void createColumns(block b, TableView<List<StringProperty>> tv){
        //create the first column for the interview name
        TableColumn<List<StringProperty>, String> interviewCol = new TableColumn<>(b.getTitle());
        interviewCol.setId("interviewColumn");
        interviewCol.setSortable(false);
        tv.getColumns().add(interviewCol);
        interviewCol.setCellValueFactory(param -> param.getValue().get(0)); //associe la colonne à la première valeur d'une ligne de données.

        //create the second column for the legends
        TableColumn<List<StringProperty>, String> legends = new TableColumn<>(" ");
        legends.setId("dimensions");
        legends.setSortable(false);
        tv.getColumns().add(legends);
        legends.setCellValueFactory(param -> param.getValue().get(1)); //associe la colonne à la deuxieme valeur d'une ligne de données.

        int tmp = 2; //used to get the right index in the list
        for (SMoment moment : b.getFirstLine().getValues()){
            TableColumn<List<StringProperty>, String> tc = new TableColumn<>(moment.name);
            tc.setId(moment.name);
            tc.setSortable(false);
            tv.getColumns().add(tc);
            int finalTmp = tmp;
            tc.setCellValueFactory(param -> param.getValue().get(finalTmp));
            tmp++;
        }
    }

    //crée une ligne de données pour la table
    public List<StringProperty> createRow(block b, int rowIndex){
        line<ArrayList<SConcreteCategory>> line = b.getDimensionLines().get(rowIndex);

        ObservableList<StringProperty> rowData = FXCollections.observableArrayList();
        rowData.add(new SimpleStringProperty(""));
        rowData.add(new SimpleStringProperty(line.getTitle()));

        // ajoute les valeurs de chaque moment
        for (ArrayList<SConcreteCategory> ccList : line.getValues()) {
            StringBuilder strValue = new StringBuilder();
            for (SConcreteCategory cc : ccList) {
                //test si c'est le dernier de la liste
                if (ccList.indexOf(cc) == ccList.size() - 1) {
                    strValue.append(cc.schemaCategory.name);
                } else {
                    strValue.append(cc.schemaCategory.name).append(", ");
                }
            }
            rowData.add(new SimpleStringProperty(strValue.toString()));
        }

        return rowData;
    }

    //rempli les lignes des dimensions avec les concreteCategories correspondantes
    public void fillLines(block b, TableView<List<StringProperty>> tv) {
        ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();
        for(int i = 0; i < b.getDimensionLines().size(); i++){
            List<StringProperty> rowData = createRow(b, i);
            data.add(rowData);
        }
        tv.setItems(data);
    }

    public void displayTable() throws IOException {
        Stage comparisonStage = new Stage();
        comparisonStage.setTitle(Configuration.langBundle.getString("comparison_table"));
        ComparisonView comparisonView = new ComparisonView(this.selectionInterviews);
        comparisonView.start(comparisonStage);
    }


    ///////////////////////////// CONTEXT MENU /////////////////////////////
    public void addColumn(TableView<List<StringProperty>> tv) {// Create the pop-up menu
        // Set the pop-up menu to show on a right-click event on the table header
        tv.setOnContextMenuRequested(event -> {
            double x = event.getX();
            int columnIndex = -1;
            double width = 0;
            for (TableColumn<?, ?> column : tv.getColumns()) {
                width += column.getWidth();
                if (width > x) {
                    columnIndex = tv.getColumns().indexOf(column);
                    break;
                }
            }

            if (columnIndex >= 0) {
                int finalColumnIndex = columnIndex;
                // Create the pop-up menu
                ContextMenu contextMenu = new ContextMenu();
                MenuItem addColumnBeforeItem = new MenuItem("Add Column Before");
                MenuItem addColumnAfterItem = new MenuItem("Add Column After");
                contextMenu.getItems().addAll(addColumnBeforeItem, addColumnAfterItem);

                // Set action listeners for the menu items
                addColumnBeforeItem.setOnAction(addColumnBeforeEvent -> {
                    // Handle "Add Column Before" action here
                    tv.getColumns().add(finalColumnIndex, new TableColumn<>("                  "));
                    tv.getColumns().get(finalColumnIndex).setSortable(false);
                    //then add columns at the end of the other tables for balance
                    for (TableView<?> tableView : getTables()){
                        if (tableView != tv){
                            tableView.getColumns().add(new TableColumn<>("                  "));
                            tv.getColumns().get(tv.getColumns().size() - 1).setSortable(false);
                        }
                    }
                    setColumnsSizesToBiggest();
                    updateColumnsSizes();
                });

                addColumnAfterItem.setOnAction(addColumnAfterEvent -> {
                    // Handle "Add Column After" action here
                    tv.getColumns().add(finalColumnIndex + 1, new TableColumn<>("                  "));
                    tv.getColumns().get(finalColumnIndex).setSortable(false);
                    //then add columns at the end of the other tables for balance
                    for (TableView<?> tableView : getTables()){
                        if (tableView != tv){
                            tableView.getColumns().add(new TableColumn<>("                  "));
                            tv.getColumns().get(tv.getColumns().size() - 1).setSortable(false);
                        }
                    }
                    setColumnsSizesToBiggest();
                    updateColumnsSizes();

                });

                contextMenu.show(tv, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public void addEmptyColumns(TableView<List<StringProperty>> tv, int length){
        int actualLength = tv.getColumns().size();
        length += 2; //add 2 because we have the interview name and the legend;
        if (actualLength < length){
            for (int i = 0; i < length - actualLength; i++){
                tv.getColumns().add(new TableColumn<>("                  "));
            }
        }
    }

    public List<TableView<?>> getTables(){
        List<TableView<?>> tableViews = new ArrayList<>();
        for (Node node : this.table.getChildren()) {
            if (node instanceof TableView) {
                tableViews.add((TableView<?>) node);
            }
        }
        return tableViews;
    }

    public void hideScroll(TableView<List<StringProperty>> tv){
        tv.widthProperty().addListener((obs, oldVal, newVal) -> {
            Set<Node> nodes = tv.lookupAll(".scroll-bar");
            for (Node node : nodes) {
                if (node instanceof ScrollBar && ((ScrollBar)node).getOrientation().equals(Orientation.HORIZONTAL)) {
                    //check if it's not the last table of the VBox
                    if (this.getTables().indexOf(tv) != this.getTables().size() - 1){
                        node.setOpacity(0);
                    }
                }
            }
        });
    }


    public List<ScrollBar> getScrollBars(){
        List<ScrollBar> scrollBars = new ArrayList<>();
        for (TableView<?> table : this.getTables()) {
            Set<Node> nodes = table.lookupAll(".scroll-bar");
            for (Node node : nodes) {
                if (node instanceof ScrollBar && ((ScrollBar)node).getOrientation().equals(Orientation.HORIZONTAL)) {
                    scrollBars.add((ScrollBar) node);
                }
            }
        }
        return scrollBars;
    }
    public void bindScroll() {
        List<ScrollBar> scrollBars = this.getScrollBars();
        AtomicBoolean isValueChanging = new AtomicBoolean(false);

        for (ScrollBar scrollBar : scrollBars) {
            scrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (!isValueChanging.get()) {
                    isValueChanging.set(true);
                    for (ScrollBar otherScrollBar : scrollBars) {
                        if (!otherScrollBar.equals(scrollBar)) {
                            otherScrollBar.setValue(newVal.doubleValue());
                        }
                    }
                    isValueChanging.set(false);
                }
            });

            scrollBar.setOnMouseDragged(event -> {
                isValueChanging.set(true);
            });

            scrollBar.setOnMouseReleased(event -> {
                isValueChanging.set(false);
            });
        }
    }


    public void getColumnsWidth(Boolean minmax){
        //get the max width of each column
        //initialize the list
        for (TableColumn<?, ?> column : this.getTables().get(0).getColumns()) {
            this.columnsWidth.add(column.getWidth());
        }
        //get the width of each column
        ArrayList<ArrayList<Double>> columnsWidthsTMP = new ArrayList<>();
        for (int i=0; i<this.getTables().get(0).getColumns().size(); i++){
            for (TableView<?> table : this.getTables()) {
                columnsWidthsTMP.add(new ArrayList<>());
                columnsWidthsTMP.get(i).add(table.getColumns().get(i).getWidth());
            }
        }
        if (minmax) {
            //get the max width of each column
            for (int i = 0; i < this.getTables().get(0).getColumns().size(); i++) {
                this.columnsWidth.set(i, Collections.max(columnsWidthsTMP.get(i)));
            }
        }
        else{
            //get the min width of each column
            for (int i = 0; i < this.getTables().get(0).getColumns().size(); i++) {
                this.columnsWidth.set(i, Collections.min(columnsWidthsTMP.get(i)));
            }
        }
    }

    public void setColumnsSizesToBiggest(){
        getColumnsWidth(true);
        for (TableView<?> table : this.getTables()) {
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(this.columnsWidth.get(table.getColumns().indexOf(column)));
            }
        }
    }
    public void setColumnsSizesToSmallest(){
        getColumnsWidth(false);
        for (TableView<?> table : this.getTables()) {
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.setPrefWidth(this.columnsWidth.get(table.getColumns().indexOf(column)));
            }
        }
    }

    public void updateColumnsSizes() {
        //if a column size in changed manually, or if we move a column then we update the other columns
        for (TableView<?> table : this.getTables()) {
            for (TableColumn<?, ?> column : table.getColumns()) {
                column.widthProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() > oldVal.doubleValue()) {
                        setColumnsSizesToBiggest();
                    }
                    else if(newVal.doubleValue() < oldVal.doubleValue()){
                        setColumnsSizesToSmallest();
                    }
                });
            }
        }
    }


}

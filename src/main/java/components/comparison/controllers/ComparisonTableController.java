package components.comparison.controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.comparison.*;
import components.comparison.appCommands.AddColumnCommand;
import components.comparison.appCommands.DelColumnCommand;
import components.comparison.appCommands.MoveColumnCommand;
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
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import persistency.newSaveSystem.SConcreteCategory;
import persistency.newSaveSystem.SMoment;

//import javax.swing.event.ChangeListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import persistency.Export.*;
import utils.GlobalVariables;

public class ComparisonTableController implements Initializable {

    private @FXML VBox table;
    private ComparisonTable comparisonTable;
    private ObservableList<String> selectionInterviews;

    private ArrayList<Double> columnsWidth = new ArrayList<>();
    private Stage comparisonStage;

    public ComparisonTableController(ObservableList<String> selectionInterviews) {
        this.table = new VBox();
        this.selectionInterviews = selectionInterviews;
        initialize(null, null);
    }

    public void undo() {
        HistoryManager.goBack();
    }

    public void redo() {
        HistoryManager.goForward();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final KeyCodeCombination keyCombUNDO = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
        final KeyCodeCombination keyCombREDO = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
        //set the undo method to the undo keyCombination
        table.setOnKeyPressed(event -> {
            if (keyCombUNDO.match(event)) {
                undo();
            }
            if (keyCombREDO.match(event)) {
                redo();
            }
        });
        fillTable(this.selectionInterviews);
        Platform.runLater(() -> {
            bindScroll();
            setColumnsSizesToBiggest();
        });

    }


    ///////////////////////////// CREATE TABLE /////////////////////////////
    //remplit la table avec les données
    public void fillTable(ObservableList<String> selectionInterviews) {
        try {
            this.comparisonTable = new ComparisonTable(selectionInterviews);

            //create table for each interview, we will fill them then
            for (block b : this.comparisonTable.getBlocks()){
                TableView<List<StringProperty>> tv = new TableView<>();
                tv.setPrefWidth(1000);
                tv.setId(b.getTitle());
                hideScroll(tv);

                this.table.getChildren().add(tv);

                //create the columns for the moments
                createColumns(b, tv);

                //fill the table with the data
                fillLines(b, tv);

                //add empty columns at the end if it's shorter than the bigger interview
                completeTable(tv, this.comparisonTable.getMaxTableLength());
                setListener(tv); //allow to display the context menu
                movingColumnListener(tv); //allow to know when a column is moved
                updateColumnsSizes();

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
        this.comparisonStage = new Stage();
        this.comparisonStage.setTitle(Configuration.langBundle.getString("comparison_table"));
        ComparisonView comparisonView = new ComparisonView(this.selectionInterviews);
        comparisonView.start(this.comparisonStage);
    }


    ///////////////////////////// MOVING COLUMNS /////////////////////////////
    //to improve, doesn't work well
    public void movingColumnListener(TableView<List<StringProperty>> tv){
        tv.setOnDragDone(event -> {
            if (event.getGestureSource() instanceof TableColumnHeader) {
                TableColumnHeader source = (TableColumnHeader) event.getGestureSource();
                TableColumnHeader target = (TableColumnHeader) event.getGestureTarget();
                int fromIndex = tv.getColumns().indexOf(source.getTableColumn());
                int toIndex = tv.getColumns().indexOf(target.getTableColumn());
                MoveColumnCommand moveColumnCommand = new MoveColumnCommand(tv, fromIndex, toIndex);
                HistoryManager.addCommand(moveColumnCommand, true);
            }
        });
    }
    ///////////////////////////// CONTEXT MENU /////////////////////////////
    public void setContextMenu(TableView<List<StringProperty>> tv, int columnIndex, ContextMenuEvent event) {
        // Create the pop-up menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addColumnBeforeItem = new MenuItem("Add Column Before");
        MenuItem addColumnAfterItem = new MenuItem("Add Column After");
        MenuItem deleteColumnItem = new MenuItem("Delete Column");
        contextMenu.getItems().addAll(addColumnBeforeItem, addColumnAfterItem, deleteColumnItem);

        // Set action listeners for the menu items
        addColumnBeforeItem.setOnAction(addColumnBeforeEvent -> {
            this.addColumn(columnIndex, tv);
        });

        addColumnAfterItem.setOnAction(addColumnAfterEvent -> {
            this.addColumn(columnIndex + 1, tv);
        });

        deleteColumnItem.setOnAction(deleteColumnEvent -> {
            this.deleteColumn(columnIndex, tv);
        });

        contextMenu.show(tv, event.getScreenX(), event.getScreenY());
    }
    public void setListener(TableView<List<StringProperty>> tv) {// Create the pop-up menu
        // Set the pop-up menu to show on a right-click event on the table header
        for (TableColumn<?, ?> column : tv.getColumns()) {
            column.setGraphic(new Label(column.getText()));
            column.setContextMenu(new ContextMenu());
            column.getGraphic().setOnContextMenuRequested(event -> {
                int columnIndex = tv.getColumns().indexOf(column);
                setContextMenu(tv, columnIndex, event);
            });
        }
    }
    public void setListener(TableColumn<List<StringProperty>, StringProperty> tc){
        tc.setGraphic(new Label(tc.getText()));
        tc.setContextMenu(new ContextMenu());
        tc.getGraphic().setOnContextMenuRequested(event -> {
            int columnIndex = tc.getTableView().getColumns().indexOf(tc);
            setContextMenu(tc.getTableView(), columnIndex, event);
        });
    }

    public void addColumn(int idx, TableView<List<StringProperty>> tv){
        // Handle "Add Column After" action here
        AddColumnCommand addColumnCommand = new AddColumnCommand(idx, tv, getTables());
        HistoryManager.addCommand(addColumnCommand, true);
        setListener((TableColumn<List<StringProperty>, StringProperty>) tv.getColumns().get(idx));
        for (TableView<List<StringProperty>> tableView : getTables()){
            if (tableView != tv){
                setListener((TableColumn<List<StringProperty>, StringProperty>) tv.getColumns().get(tv.getColumns().size() - 1));
            }
        }
        setColumnsSizesToBiggest();
        updateColumnsSizes();
    }

    public void deleteColumn(int idx, TableView<List<StringProperty>> tv){
        //delete column on right click
        DelColumnCommand delColumnCommand = new DelColumnCommand(idx, tv, getTables());
        HistoryManager.addCommand(delColumnCommand, true);
        updateColumnsSizes();
    }


    public void completeTable(TableView<List<StringProperty>> tv, int length){
        int actualLength = tv.getColumns().size();
        length += 2; //add 2 because we have the interview name and the legend;
        if (actualLength < length){
            for (int i = 0; i < length - actualLength; i++){
                TableColumn<List<StringProperty>, StringProperty> tc = new TableColumn<>("                  ");
                tv.getColumns().add(tc);
                setListener(tc);
            }
        }
    }

    public List<TableView<List<StringProperty>>> getTables(){
        List<TableView<List<StringProperty>>> tableViews = new ArrayList<>();
        for (Node node : this.table.getChildren()) {
            if (node instanceof TableView) {
                tableViews.add((TableView<List<StringProperty>>) node);
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

    public void exportToExcel(){
        ComparisonTableDataExtractor extractor = new ComparisonTableDataExtractor();
        List<List<List<String>>> containerData = extractor.extractContainerData(this.table);

        String path = Configuration.getProjectsPath()[0];
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(path).getParentFile());
        fileChooser.setInitialFileName(GlobalVariables.getGlobalVariables().getCurrentProjectPath());
        fileChooser.setTitle(Configuration.langBundle.getString("export_to_excel"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(this.comparisonStage);
        if(saveFile != null) {
            ExcelExporter.exportToExcel(containerData, saveFile.getAbsolutePath());
        }
    }


}

package components.comparison.controllers;

import application.configuration.Configuration;
import components.comparison.ComparisonTable;
import components.comparison.ComparisonView;
import components.comparison.block;
import components.comparison.line;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Interview;
import persistency.newSaveSystem.SConcreteCategory;
import persistency.newSaveSystem.SMoment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComparisonTableController implements Initializable {

    @FXML
    private VBox table;
    private ObservableList<String> selectionInterviews;

    public ComparisonTableController(ObservableList<String> selectionInterviews) {
        this.table = new VBox();
        this.selectionInterviews = selectionInterviews;
        initialize(null, null);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTable(this.selectionInterviews);
    }

    //remplit la table avec les données
    public void fillTable(ObservableList<String> selectionInterviews) {
        try {
            ComparisonTable ct = new ComparisonTable(selectionInterviews);
            //create table for each interview, we will fill them then
            for (block b : ct.getBlocks()){
                TableView<List<StringProperty>> tv = new TableView<>();
                tv.setPrefWidth(1000);
                tv.setId(b.getTitle());
                this.table.getChildren().add(tv);

                //create the columns for the moments
                createColumns(b, tv);

                //fill the table with the data
                fillLines(b, tv);

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
}

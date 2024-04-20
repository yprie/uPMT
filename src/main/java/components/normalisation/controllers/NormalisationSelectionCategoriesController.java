package components.normalisation.controllers;

import application.configuration.Configuration;
import components.normalisation.SelectionCategoriesView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import models.*;
import utils.GlobalVariables;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NormalisationSelectionCategoriesController implements Initializable {

    @FXML
    private Button validateButton;
    @FXML
    private TableView<CategoryRowModel> categoriesTable;
    private final ObservableList<CategoryRowModel> categories = FXCollections.observableArrayList();

    private SelectedCategoriesController selectedCategoriesController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCategoriesController = new SelectedCategoriesController();


        categoriesTable.setEditable(true);

        // Créer et ajouter les colonnes à la table
        TableColumn<CategoryRowModel, Boolean> selectColumn = new TableColumn<>("Selection");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        TableColumn<CategoryRowModel, String> nameColumn = new TableColumn<>("Category Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<CategoryRowModel, Number> usesColumn = new TableColumn<>("Uses");
        usesColumn.setCellValueFactory(cellData -> cellData.getValue().getNbOfUses());

        TableColumn<CategoryRowModel, String> propertiesColumn = new TableColumn<>("Properties");
        propertiesColumn.setCellValueFactory(cellData -> {
            CategoryRowModel rowModel = cellData.getValue();
            String properties = rowModel.getProperties().stream()
                    .map(property -> "• " + property.getName())
                    .collect(Collectors.joining("\n"));
            return new SimpleStringProperty(properties);
        });


        TableColumn<CategoryRowModel, String> interviewsColumn = new TableColumn<>("Associated Interviews (with Moments)");
        interviewsColumn.setCellValueFactory(cellData -> {
            CategoryRowModel rowModel = cellData.getValue();
            StringBuilder moments = new StringBuilder();
            for (Interview interview : rowModel.getInterviews()) {
                moments.append("• ").append(interview.getTitle()).append(":\n");

                List<String> momentNames = new ArrayList<>();
                for (Moment moment : rowModel.getInterviewMomentsMap().get(interview)) {
                    momentNames.add(moment.getName());
                }
                if (momentNames != null) {
                    for (String momentName : momentNames) {
                        moments.append("\t• ").append(momentName).append("\n");
                    }
                }
            }
            return new SimpleStringProperty(moments.toString());
        });

        categoriesTable.getColumns().addAll(selectColumn, nameColumn, usesColumn, propertiesColumn, interviewsColumn);

        // Remplir la table avec les catégories
        for (SchemaFolder folder : GlobalVariables.getSchemaTreeRoot().foldersProperty()) {
            for (SchemaCategory category : folder.categoriesProperty()) {
                categories.add(new CategoryRowModel(category));
            }
        }

        categoriesTable.setItems(categories);
    }

    public void execute() {
        System.out.println("NormalisationSelectionCategoriesControler.execute");
    }

    public void selectCategories() {
        Stage SelectStage = new Stage();
        SelectStage.setTitle(Configuration.langBundle.getString("categories_normalization"));
        SelectionCategoriesView selectionCategoriesView = new SelectionCategoriesView();
        try {
            selectionCategoriesView.start(SelectStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CategoryRowModel> getSelectedCategories() {
        return categories.stream()
                .filter(CategoryRowModel::isSelected)
                .collect(Collectors.toList());
    }

    // lorsque l'utilisateur clique sur le bouton "Valider"
    // on récupère les catégories sélectionnées et on les envoie à la vue suivante
    // la vue suivante est SelectedCategoriesView
    @FXML
    private void handleValidateButton(ActionEvent event) {
        List<CategoryRowModel> selectedCategories = getSelectedCategories();
        for(CategoryRowModel category : selectedCategories) {
            System.out.println("Selected category: " + category.getName());
        }

        // Utiliser la même instance de SelectedCategoriesController pour lancer la vue
        selectedCategoriesController.launchView(selectedCategories);
    }


}

package components.normalisation.controllers;

import components.normalisation.SelectedCategoriesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.CategoryRowModel;
import models.Moment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectedCategoriesController implements Initializable {

    private Button validateButton;
    @FXML
    private GridPane selectedCategoriesGrid;
    private final ObservableList<CategoryRowModel> categories = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        fillTable(categories);

        // ajout des bordures
        selectedCategoriesGrid.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
        selectedCategoriesGrid.setHgap(10);
        selectedCategoriesGrid.setVgap(10);







    }

// cette fonction permet de remplir le tableau avec les catégories sélectionnées

    public void fillTable(List<CategoryRowModel> categories) {
        // Supprimer tous les enfants du GridPane
        selectedCategoriesGrid.getChildren().clear();

        // Ajouter les en-têtes des colonnes
        selectedCategoriesGrid.add(new javafx.scene.control.Label("Nom de la catégorie"), 0, 0);
        int columnIndex = 1;
        int rowIndex = 1; // Index de la ligne actuelle
        System.out.println("Nombre de catégories : " + categories.size());
        // Ajouter les catégories à la table
        for (CategoryRowModel category : categories) {
            // Ajouter le nom de la catégorie
            System.out.println("Première catégorie : " + category.getName());
            selectedCategoriesGrid.add(new Label(category.getName()), 0, rowIndex);


            // Ajouter les moments associés

            StringBuilder moments = new StringBuilder();
            for (Moment moment : category.getMoments()) {
                moments.append(moment.getName()).append("\n");
                selectedCategoriesGrid.add(new javafx.scene.control.Label(moment.getName()), columnIndex, 0);
                System.out.println("chaque instance : " + category.getConcreteCategory(moment).toString());
                columnIndex++;

            }
            //selectedCategoriesGrid.add(new javafx.scene.control.Label(moments.toString()), 1, rowIndex);




            rowIndex++;
        }




    }


    // fonction pour lancer la vue avec les catégories sélectionnées
    public void launchView(List<CategoryRowModel> categories) {
        Stage stage = new Stage();
        SelectedCategoriesView selectedCategoriesView = new SelectedCategoriesView();
        selectedCategoriesView.setCategories(categories);

        try {
            selectedCategoriesView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }






    }


    public void setCategories(List<CategoryRowModel> categories) {
        this.categories.addAll(categories);
    }
}

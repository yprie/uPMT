package components.normalisation.controllers;

import application.configuration.Configuration;
import components.normalisation.SelectionCategoriesView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.CategoryRowModel;
import models.SchemaCategory;
import utils.GlobalVariables;

import java.net.URL;
import java.util.ResourceBundle;

public class NormalisationSelectionCategoriesControler implements Initializable {

    @FXML
    private TableView<CategoryRowModel> categoriesTable;
    private ObservableList<CategoryRowModel> categories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    // remplir la table avec les catégories
        GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();
        for (SchemaCategory category : globalVariables.getProject().getSchemaTreeRoot().categoriesProperty()) {
            categories.add(new CategoryRowModel(category));
        }
        // affichage des colonnes (pour vérification) de la tableView
        for (int i = 0; i < categoriesTable.getColumns().size(); i++) {
            System.out.println(categoriesTable.getColumns().get(i));
        }

        categoriesTable.setItems(categories);



    }

    public void execute() {
        System.out.println("NormalisationSelectionCategoriesControler.execute");
    }

    public void selectCategories() {
        Stage SelectStage = new Stage();
        // cette ligne permet de traduire le titre de la fenêtre
        SelectStage.setTitle(Configuration.langBundle.getString("categories_normalization"));
        SelectionCategoriesView selectionCategoriesView = new SelectionCategoriesView();
        try {
            selectionCategoriesView.start(SelectStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

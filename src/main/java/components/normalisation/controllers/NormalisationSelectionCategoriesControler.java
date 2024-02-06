package components.normalisation.controllers;

import application.configuration.Configuration;
import components.normalisation.SelectionCategoriesView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.CategoryRowModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NormalisationSelectionCategoriesControler implements Initializable {

    @FXML
    private TableView<CategoryRowModel> categoriesTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


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

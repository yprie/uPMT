package components.normalisation;

import application.configuration.Configuration;
import components.normalisation.controllers.SelectedCategoriesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.CategoryRowModel;

import java.util.List;

public class SelectedCategoriesView extends Application {

    SelectedCategoriesController scc = new SelectedCategoriesController();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Normalisation/SelectedCategoriesView.fxml"));
        fxmlLoader.setController(scc);
        fxmlLoader.setResources(Configuration.langBundle);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setCategories(List<CategoryRowModel> categories) {
        scc.setCategories(categories);
    }
}

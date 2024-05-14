package components.normalisation;

import application.configuration.Configuration;
import components.normalisation.controllers.NormalisationSelectionCategoriesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SelectionCategoriesView  extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Normalisation/SelectionCategoriesView.fxml"));
        NormalisationSelectionCategoriesController ncc = new NormalisationSelectionCategoriesController();
        fxmlLoader.setController(ncc);
        fxmlLoader.setResources(Configuration.langBundle);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();



    }
}

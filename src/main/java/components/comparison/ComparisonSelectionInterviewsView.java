package components.comparison;

import application.configuration.Configuration;
import components.comparison.controllers.ComparisonSelectionInterviewsControler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ComparisonSelectionInterviewsView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Project/ComparisonSelectionInterviewsView.fxml"));
        ComparisonSelectionInterviewsControler cc = new ComparisonSelectionInterviewsControler();
        fxmlLoader.setController(cc);
        fxmlLoader.setResources(Configuration.langBundle);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
}

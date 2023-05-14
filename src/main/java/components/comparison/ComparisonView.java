package components.comparison;

import application.configuration.Configuration;
import components.comparison.controllers.ComparisonTableController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ComparisonView extends Application {

    private final ObservableList<String> selectionInterviews;

    public ComparisonView(ObservableList<String> selectionInterviews){
        this.selectionInterviews = selectionInterviews;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Comparison/ComparisonView.fxml"));
        ComparisonTableController controller = new ComparisonTableController(this.selectionInterviews);
        fxmlLoader.setController(controller);
        fxmlLoader.setResources(Configuration.langBundle);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        stage.setScene(scene);

        stage.show();
    }
}
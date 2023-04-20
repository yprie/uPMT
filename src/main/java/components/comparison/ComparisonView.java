package components.comparison;

import components.comparison.controllers.ComparisonTableController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ComparisonView extends Application {

    private VBox table;

    public ComparisonView() {
        this.table = new VBox();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Comparison/ComparisonView.fxml"));
        ComparisonTableController controller = new ComparisonTableController();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

//        ComparisonViewController controller = fxmlLoader.getController();
//        // do something with the controller

        stage.show();
    }
}
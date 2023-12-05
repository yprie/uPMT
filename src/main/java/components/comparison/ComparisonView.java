package components.comparison;

import application.appCommands.ApplicationCommand;
import application.appCommands.ApplicationCommandFactory;
import application.configuration.Configuration;
import application.history.HistoryManager;
import components.comparison.controllers.ComparisonTableController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import utils.GlobalVariables;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ComparisonView extends Application {

    private final ObservableList<String> selectionInterviews;
    private final UUID startingId = HistoryManager.getCurrentCommandId();     //save the current command id

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
        controller.setShortcuts();
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        stage.setScene(scene);
        GlobalVariables.getGlobalVariables().setComparisonState(true);
        GlobalVariables.getGlobalVariables().setId(startingId);
        stage.show();

    }
}
package utils.popups;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.DialogState;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategy;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WarningPopup implements Initializable {

    private Stage stage;
    private String warning;

    @FXML private Label text;
    @FXML private Button cancel;
    @FXML private Button confirm;

    public WarningPopup(Stage stage, String instruction) {
        this.warning = instruction;
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text.setText(warning);
        confirm.setDefaultButton(true);

        confirm.setOnAction(actionEvent -> {
            stage.close();
        });
    }

    public static WarningPopup display(String instruction){
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Warning");
        WarningPopup controller = new WarningPopup(stage, instruction);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/utils/popups/Warning.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            VBox layout = loader.load();
            Scene main = new Scene(layout);
            stage.setScene(main);
            stage.showAndWait();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

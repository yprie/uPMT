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

public class TextEntryController implements Initializable {

    private Stage stage;
    private DialogState state;
    private String instruction;
    private String value;
    private int maxChar;

    @FXML private Label text;
    @FXML private Button cancel;
    @FXML private Button confirm;
    @FXML private Pane paneForInput;
    private AutoSuggestionsTextField input;

    public TextEntryController(Stage stage, String instruction, String value, int maxChar) {
        this.instruction = instruction;
        this.value = value;
        this.maxChar = maxChar;
        this.stage = stage;
        this.state = DialogState.CLOSED;
        input = new AutoSuggestionsTextField();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneForInput.getChildren().add(input);
        text.setText(instruction);
        input.setText(value);
        input.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length() > maxChar)
                input.setText(t1.substring(0, maxChar));
        });
        confirm.setDefaultButton(true);

        confirm.setOnAction(actionEvent -> {
            value = input.getText();
            state = DialogState.SUCCESS;
            stage.close();
        });
        cancel.setOnAction(actionEvent -> {
            stage.close();
        });
    }

    public static TextEntryController enterText(String instruction, String value, int maxChar, SuggestionStrategy newSuggestionStrategy) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("input"));
        TextEntryController controller = new TextEntryController(stage, instruction, value, maxChar);
        controller.input.setStrategy(newSuggestionStrategy);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/utils/popups/TextEntry.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            VBox layout = loader.load();
            Scene main = new Scene(layout);
            stage.setScene(main);
            stage.showAndWait();
            return controller;
        } catch (IOException e) {
            // TODO Exit Program
            e.printStackTrace();
        }
        return null;
    }

    public void setStrategy(SuggestionStrategy newSuggestionStrategy) {
        input.setStrategy(newSuggestionStrategy);
    }

    public DialogState getState() { return state; }
    public String getValue() { return value; }
}

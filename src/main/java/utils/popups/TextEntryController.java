package utils.popups;

import application.configuration.Configuration;
import application.project.controllers.NewProjectController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.DialogState;

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
    @FXML private TextField input;
    @FXML private Button cancel;
    @FXML private Button confirm;

    public TextEntryController(Stage stage, String instruction, String value, int maxChar) {
        this.instruction = instruction;
        this.value = value;
        this.maxChar = maxChar;
        this.stage = stage;
        this.state = DialogState.CLOSED;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public static TextEntryController enterText(String instruction, String value, int maxChar) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("input"));
        TextEntryController controller = new TextEntryController(stage, instruction, value, maxChar);
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

    public DialogState getState() { return state; }
    public String getValue() { return value; }
}

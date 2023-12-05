package utils.popups;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.DialogState;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MergerPopup implements Initializable {

    private Stage stage;
    private DialogState state;
    private String instruction;

    @FXML private Label text;
    @FXML private Button cancel;
    @FXML private Button confirm;

    public MergerPopup(Stage stage, String instruction) {
        this.stage = stage;
        this.state = DialogState.CLOSED;
        this.instruction = instruction;


    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text.setText(instruction);
        confirm.setDefaultButton(true);

        confirm.setOnAction(actionEvent -> {
            state = DialogState.SUCCESS;
            stage.close();
        });

        cancel.setOnAction(actionEvent -> {
            state = DialogState.CLOSED;
            stage.close();
        });
    }

    public static MergerPopup display(String instruction) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("merge_confirmation_title"));

        MergerPopup controller = new MergerPopup(stage, instruction);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/utils/popups/MergingPopup.fxml"));
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

    public DialogState getState() {
        return state;
    }
}

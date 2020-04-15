package components.modelisationSpace.justification.justificationCell;

import application.configuration.Configuration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import models.Descripteme;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditJustificationCell implements Initializable {

    private Descripteme descripteme;
    private Descripteme descriptemeCopy;

    private @FXML Label descriptemeText;
    private @FXML Button shiftLeft, shiftRight;
    private @FXML RadioButton beginningButton, endButton;

    public EditJustificationCell(Descripteme descripteme) {
        this.descripteme = descripteme;
        this.descriptemeCopy = descripteme.duplicate();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        descriptemeText.textProperty().bind(descriptemeCopy.getSelectionProperty());

        shiftLeft.setOnAction(actionEvent -> {
            if(beginningButton.isSelected()){
                descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex()-1, descriptemeCopy.getEndIndex());
            }
            else if(endButton.isSelected()){
                descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex()-1);
            }
        });

        shiftRight.setOnAction(actionEvent -> {
            if(beginningButton.isSelected()){
                descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex()+1, descriptemeCopy.getEndIndex());
            }
            else if(endButton.isSelected()){
                descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex()+1);
            }
        });
    }

    public static EditJustificationCell edit(Descripteme descripteme, Window primaryStage) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("descripteme_edit"));
        EditJustificationCell controller = new EditJustificationCell(descripteme);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/Justification/JustificationCell/EditJustificationCell.fxml"));
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

    private void onRadioButtonChange() {
        //TODO disable a button if it is to a border of the entire text.
    }

}

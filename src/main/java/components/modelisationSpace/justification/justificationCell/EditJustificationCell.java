package components.modelisationSpace.justification.justificationCell;

import application.configuration.Configuration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private Stage stage;
    private Descripteme descripteme;
    private Descripteme descriptemeCopy;

    private @FXML
    TextArea textArea;
    private @FXML
    Button frontShiftLeft, frontShiftRight, endShiftLeft, endShiftRight, frontShiftLeftBy5, frontShiftRightBy5, endShiftLeftBy5, endShiftRightBy5, cancelButton, confirmButton;


    public EditJustificationCell(Stage stage, Descripteme descripteme) {
        this.stage = stage;
        this.descripteme = descripteme;
        this.descriptemeCopy = new Descripteme(descripteme.getInterviewText(), descripteme.getStartIndex(), descripteme.getEndIndex());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.textProperty().bind(descriptemeCopy.getSelectionProperty());

        frontShiftLeft.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex() - 1, descriptemeCopy.getEndIndex());
            onDescriptemeUpdate();
        });
        endShiftLeft.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex() - 1);
            onDescriptemeUpdate();
        });
        frontShiftLeftBy5.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex() - 5, descriptemeCopy.getEndIndex());
            onDescriptemeUpdate();
        });
        endShiftLeftBy5.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex() - 5);
            onDescriptemeUpdate();
        });

        frontShiftRight.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex() + 1, descriptemeCopy.getEndIndex());
            onDescriptemeUpdate();
        });
        endShiftRight.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex() + 1);
            onDescriptemeUpdate();
        });

        frontShiftRightBy5.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex() + 5, descriptemeCopy.getEndIndex());
            onDescriptemeUpdate();
        });
        endShiftRightBy5.setOnAction(actionEvent -> {
            descriptemeCopy.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex() + 5);
            onDescriptemeUpdate();
        });


        cancelButton.setOnAction(actionEvent -> {
            stage.close();
        });

        confirmButton.setOnAction(actionEvent -> {
            descripteme.modifyIndex(descriptemeCopy.getStartIndex(), descriptemeCopy.getEndIndex());
            stage.close();
        });
    }

    public static EditJustificationCell edit(Descripteme descripteme, Window primaryStage) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("descripteme_edit"));
        EditJustificationCell controller = new EditJustificationCell(stage, descripteme);
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
            e.printStackTrace();
        }
        return null;
    }

    private void onDescriptemeUpdate() {
        frontShiftLeft.setDisable(false);
        frontShiftRight.setDisable(false);
        endShiftLeft.setDisable(false);
        endShiftRight.setDisable(false);

        frontShiftLeftBy5.setDisable(false);
        frontShiftRightBy5.setDisable(false);
        endShiftLeftBy5.setDisable(false);
        endShiftRightBy5.setDisable(false);

        if (descriptemeCopy.getStartIndex() == 0)
            frontShiftLeft.setDisable(true);
        else if (descriptemeCopy.getFragmentText().length() == 1)
            frontShiftRight.setDisable(true);
        if (descriptemeCopy.getEndIndex() == descriptemeCopy.getInterviewText().getText().length())
            endShiftRight.setDisable(true);
        else if (descriptemeCopy.getFragmentText().length() == 1)
            endShiftLeft.setDisable(true);

        if (descriptemeCopy.getStartIndex() <= 5)
            frontShiftLeftBy5.setDisable(true);
        else if (descriptemeCopy.getFragmentText().length() <= 5)
            frontShiftRightBy5.setDisable(true);
        if ((descriptemeCopy.getInterviewText().getText().length() - descriptemeCopy.getEndIndex()) <= 5)
            endShiftRightBy5.setDisable(true);
        else if (descriptemeCopy.getFragmentText().length() <= 5)
            endShiftLeftBy5.setDisable(true);

    }

}

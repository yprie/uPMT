package components.interviewSelector.controllers;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Interview;
import utils.DialogState;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifyInterviewController implements Initializable {
    @FXML private TextField participantName;
    @FXML private DatePicker interviewDate;
    @FXML private TextArea interviewComment;
    @FXML private Label interviewTitle;
    @FXML private Label textPreview;
    @FXML private Button validateButton;
    private Interview currentInterview;
    private DialogState state;
    private Stage stage;

    public ModifyInterviewController(Stage stage) {
        this.stage = stage;
        this.state = DialogState.CLOSED;
    }

    public void setInterview(Interview interview){
       currentInterview = interview;
   }

    public DialogState getState() {
        return state;
    }

    public static ModifyInterviewController createController(){
       Stage stage = new Stage(StageStyle.UTILITY);
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.setTitle(Configuration.langBundle.getString("edit_interview"));
       ModifyInterviewController controller = new ModifyInterviewController(stage);
       try {
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(controller.getClass().getResource("/views/Project/ModifyInterviewView.fxml"));
           loader.setController(controller);
           loader.setResources(Configuration.langBundle);
           AnchorPane layout =  loader.load();
           Scene main = new Scene(layout);
           stage.setScene(main);
           return controller;
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        participantName.textProperty().addListener((observable, oldValue, newValue) -> {
            interviewTitle.setText(getTitle(newValue, interviewDate.getValue()));
        });
        interviewDate.valueProperty().addListener((observableValue, oldValue, newValue) ->
                interviewTitle.setText(getTitle(participantName.getText(), newValue)));
    }

    public void initData(){
        interviewDate.setValue(currentInterview.getDate());
        textPreview.setText(currentInterview.getInterviewText().getText());
        participantName.setText(currentInterview.getParticipantName());
        interviewComment.setText(currentInterview.getComment());
        interviewTitle.setText(currentInterview.getTitle());
    }

    public void show(){
        stage.showAndWait();
    }

    public LocalDate getDate(){
        return interviewDate.getValue();
    }
    public String getComment(){
        return interviewComment.getText();
    }
    public String getParticipantName(){
        return participantName.getText();
    }

    @FXML
    public void modifyInterview(){
        state = DialogState.SUCCESS;
        stage.close();
    }
    @FXML
    private void closeWindow() {
        state = DialogState.CLOSED;
        stage.close();

    }

    private String getTitle(String participantName, LocalDate date) {
        String dateStr;
        if (date == null) {
            dateStr = "";
        }
        else {
            dateStr = date.toString();
        }
        if(!dateStr.equals("")) {
            return participantName + "_" + dateStr;
        }
        else {
            return participantName;
        }
    }
}

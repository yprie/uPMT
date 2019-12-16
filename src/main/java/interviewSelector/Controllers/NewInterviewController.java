package interviewSelector.controllers;

import interviewSelector.Models.Interview;
import application.Configuration.Configuration;
import Components.InterviewPanel.Models.InterviewText;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import utils.InterviewUtils;
import utils.DialogState;

import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.*;
import java.io.IOException;
import java.io.File;

public class NewInterviewController implements Initializable {
    private DialogState state;
    private Stage stage;
    private Interview resultInterview;
    private File chosenFile;
    @FXML private TextField participantName;
    @FXML private Label chosenFilename;
    @FXML private Label interviewTitle;
    @FXML private Label interviewTextExtract;
    @FXML private DatePicker interviewDate;
    @FXML private TextArea interviewComment;
    @FXML private Button validateButton;

    public static NewInterviewController createNewInterview() {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("new_interview"));
        NewInterviewController controller = new NewInterviewController(stage);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/Project/NewInterviewView.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            AnchorPane layout = (AnchorPane) loader.load();
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

    public NewInterviewController(Stage stage) {
        this.stage = stage;
        this.state = DialogState.CLOSED;
    }

    public DialogState getState() { return state; }
    public Interview getCreatedInterview() { return resultInterview; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateButton.setDisable(true);
        validateForm();
    }

    @FXML
    private void createInterview(){
        if (validateForm()) {

            resultInterview = new Interview(interviewTitle.getText(),
                    participantName.getText(),
                    interviewDate.getValue(),
                    new InterviewText(interviewComment.getText())
            );
            state = DialogState.SUCCESS;
            stage.close();
        }
    }

    @FXML
    private void closeWindow() {
        state = DialogState.CLOSED;
        stage.close();
    }

    @FXML
    private void openFileChooser(){
        FileChooser fileChooser = new FileChooser();
        ArrayList<String> l = new ArrayList<String>();
        l.add("*.txt");
        l.add("*.text");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(Configuration.langBundle.getString("text_files"),l)
        );
        fileChooser.setTitle(Configuration.langBundle.getString("select_verbatim"));
        chosenFile = fileChooser.showOpenDialog(stage);
        if(chosenFile != null){
            chosenFilename.setText(chosenFile.getPath());

            // >> was commented
            /*
			if (interviewTitle.getText().equals("") && interviewDate.getValue().toString()!= null) {
                interviewTitle.setText(chosenFile.getName().replaceFirst("[.][^.]+$", "")
						+ "_" + interviewDate.getValue().toString());
			}
             */
			// <<

            interviewTextExtract.setText(InterviewUtils.getExtract(chosenFile));
        }
        else {
            chosenFilename.setText("/");
            interviewTextExtract.setText("");
        }
        validateForm();
    }

    // Helpers:
    private boolean validateForm() {
        boolean formIsValide = (!participantName.getText().equals("") &&
                interviewDate.getValue() != null &&
                chosenFile != null
        );
        validateButton.setDisable(!formIsValide);
        return formIsValide;
    }

    private void setTitle() {
        if (interviewDate.getValue() != null) {
            String date = interviewDate.getValue().toString();
            if(!date.equals("")) {
                interviewTitle.setText(participantName.getText() + "_" + date);
            }
            if(participantName.getText().equals("")) {
                interviewTitle.setText("");
            }
        }
    }

    // Events:
    @FXML
    private void participantNameOnKeyReleased(KeyEvent keyEvent) {
        setTitle();
        validateForm();
    }

    @FXML
    private void interviewDateOnKeyReleased(KeyEvent keyEvent) {
        setTitle();
        validateForm();
    }

    @FXML
    private void interviewDateOnAction(ActionEvent actionEvent) {
        setTitle();
        validateForm();
    }
}

package components.interviewSelector.controllers;

import models.Interview;
import application.configuration.Configuration;
import models.InterviewText;
import models.Moment;
import models.RootMoment;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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

import java.io.FileInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

            String res = "error during text loading !";
            byte[] data = new byte[(int) chosenFile.length()];
            try {
                FileInputStream fis = new FileInputStream(chosenFile);
                //fis.read(data);
                res = new String(fis.readAllBytes());
                System.out.println(res);
                fis.close();
/*                res = new String(data, StandardCharsets.UTF_16);*/
            } catch (IOException e) {
                e.printStackTrace();
            }

            RootMoment rm = new RootMoment();
            rm.addMoment(new Moment(Configuration.langBundle.getString("moment") + " 1"));
            rm.addMoment(new Moment(Configuration.langBundle.getString("moment") + " 2"));

            LocalDate date = interviewDate.getValue();
            if (interviewDate.getValue() == null) {
                date = LocalDateTime.now().toLocalDate();
            }
            resultInterview = new Interview(
                    participantName.getText(),
                    date,
                    new InterviewText(res),
                    rm
            );
            resultInterview.setComment(interviewComment.getText());
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

            String res = "error during text loading !";
            try {
                FileInputStream fis = new FileInputStream(chosenFile);
                res = new String(fis.readAllBytes());
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(res.length() > 0)
                interviewTextExtract.setText(res.substring(0, Math.min(res.length(),100)) + " ...");
        }
        else {
            chosenFilename.setText("/");
            interviewTextExtract.setText("");
        }
        validateForm();
    }

    // Helpers:
    private boolean validateForm() {
        boolean formIsValide = (!participantName.getText().equals("") && chosenFile != null);
        validateButton.setDisable(!formIsValide);
        return formIsValide;
    }

    private void setTitle() {
        interviewTitle.setText(Interview.getTitle(participantName.getText(), interviewDate.getValue()));
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

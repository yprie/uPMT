package components.comparison.controllers;

import application.configuration.Configuration;
import components.comparison.ComparisonSelectionInterviewsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Interview;
import utils.DialogState;
import utils.GlobalVariables;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ComparisonSelectionInterviewsControler implements Initializable {

    private ObservableList<Interview> interviews;
    private ObservableList<String> selectionInterviews;
    @FXML
    public Button btncancel;
    @FXML
    private Button validateButton;
    @FXML
    private ListView<CheckBox> interviewList;

    public ComparisonSelectionInterviewsControler() {
        this.interviews = GlobalVariables.getGlobalVariables().getProject().interviewsProperty();
        this.selectionInterviews = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.validateButton.setDisable(true);
        populateInterviewList();
        validateSelection();
    }

    public ObservableList<String> getSelectionInterviews(){
        return this.selectionInterviews;
    }


    public void closeWindow() {
        Stage stage = (Stage) btncancel.getScene().getWindow();
        stage.close();
    }

    public void displayTable() {
        try {
            ComparisonTableController cc = new ComparisonTableController(this.selectionInterviews);
            closeWindow();
            cc.displayTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateSelection(){
        boolean isValidSelect = 0 < this.selectionInterviews.size() && this.selectionInterviews.size() <= 3;
        this.validateButton.setDisable(!isValidSelect);
    }


    public void selectInterviews() throws IOException {
        Stage SelectStage = new Stage();
        SelectStage.setTitle(Configuration.langBundle.getString("interview_select"));
        ComparisonSelectionInterviewsView cv = new ComparisonSelectionInterviewsView();
        cv.start(SelectStage);
    }

    private void populateInterviewList() {
        for (Interview interview : interviews) {
            CheckBox checkBox = new CheckBox(interview.getTitle());
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (checkBox.isSelected()) {
                    selectionInterviews.add(interview.getTitle());
                } else {
                    selectionInterviews.remove(interview.getTitle());
                }
                validateSelection();
            });
            interviewList.getItems().add(checkBox);
        }
    }
}

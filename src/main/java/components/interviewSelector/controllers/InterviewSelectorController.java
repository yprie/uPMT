package components.interviewSelector.controllers;

import application.configuration.Configuration;
import components.interviewSelector.InterviewSelectorCell;
import components.interviewSelector.models.Interview;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewSelectorController implements Initializable  {

    @FXML private ListView<Interview> interviewList;
    @FXML private Button addInterviewButton;

    private ObservableList<Interview> interviews;
    private ListChangeListener<Interview> listChangeListener;

    private ObservableValue<Interview> selectedInterview;
    private ChangeListener<Interview> selectedInterviewChanged;

    private InterviewSelectorCommandFactory commandFactory;

    public InterviewSelectorController(ObservableList<Interview> interviews, ObservableValue<Interview> selectedInterview, InterviewSelectorCommandFactory commandFactory) {
        this.interviews = interviews;
        this.selectedInterview = selectedInterview;
        this.commandFactory = commandFactory;
    }

    public static Node createInterviewSelector(InterviewSelectorController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(controller.getClass().getResource("/views/InterviewSelector/InterviewSelector.fxml"));
        fxmlLoader.setResources(Configuration.langBundle);
        fxmlLoader.setController(controller);
        try {
            return fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addInterviewButton.setOnAction(event -> { commandFactory.createNewInterview().execute(); });
        interviewList.setCellFactory(listView -> new InterviewSelectorCell(commandFactory));
        bind(interviews);
        for(Interview i: interviews){
            this.interviewList.getItems().add(i);
        }
        interviewList.getSelectionModel().select(interviewList.getItems().indexOf(selectedInterview.getValue()));
    }

    private void bind(ObservableList<Interview> interviews) {
        this.interviews = interviews;
        listChangeListener = c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    this.interviewList.getItems().addAll(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    this.interviewList.getItems().removeAll(c.getRemoved());
                }
            }
        };
        interviews.addListener(listChangeListener);

        selectedInterviewChanged = (observable, oldValue, newValue) -> {
            interviewList.getSelectionModel().select(interviewList.getItems().indexOf(newValue));
        };
        selectedInterview.addListener(selectedInterviewChanged);
    }

    public void unbind() {
        if(this.interviews != null && listChangeListener != null) {
            this.interviews.removeListener(listChangeListener);
        }
        if(selectedInterview != null && selectedInterviewChanged != null)
            selectedInterview.removeListener(selectedInterviewChanged);
    }
}

package interviewSelector.controllers;

import Components.InterviewPanel.Models.InterviewText;
import application.Configuration.Configuration;
import application.History.HistoryManager;
import interviewSelector.InterviewSelectorCell;
import interviewSelector.Models.Interview;
import interviewSelector.commands.InterviewSelectorCommandFactory;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import utils.DialogState;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InterviewSelectorController implements Initializable  {

    @FXML private ListView<Interview> interviewList;
    @FXML private Button addInterviewButton;

    private ObservableList<Interview> interviews;
    private ListChangeListener<Interview> listChangeListener;
    private InterviewSelectorCommandFactory commandFactory;

    public InterviewSelectorController(ObservableList<Interview> interviews, InterviewSelectorCommandFactory commandFactory) {
        this.interviews = interviews;
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

        addInterviewButton.setOnAction(event -> {
            NewInterviewController controller = NewInterviewController.createNewInterview();
            if(controller.getState() == DialogState.SUCCESS){
                HistoryManager.addCommand(commandFactory.addInterview(controller.getCreatedInterview()), true);
            }
        });

        interviewList.setCellFactory(listView -> {return new InterviewSelectorCell(commandFactory); });
        bind(interviews);
        for(Interview i: interviews){
            this.interviewList.getItems().add(i);
        }
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
    }

    public void unbind() {
        if(this.interviews != null && listChangeListener != null) {
            this.interviews.removeListener(listChangeListener);
        }
    }
}

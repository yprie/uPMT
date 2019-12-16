package interviewSelector;

import interviewSelector.Models.Interview;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterviewSelector extends AnchorPane implements Initializable  {

    @FXML private ListView<Interview> interviewList;
    @FXML private Button addInterviewButton;
    private ObservableList<Interview> interviews;
    private ListChangeListener<Interview> listChangeListener;

    public InterviewSelector() {
        this.interviewList = new ListView<>();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/InterviewTree/InterviewTree.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        interviewList.setCellFactory(listView -> {return new InterviewListCell(); });
    }

    public void setInterviews(ObservableList<Interview> interviews) {
        setupListener(interviews);
        for(Interview i: interviews)
            this.interviewList.getItems().add(i);
    }
    
    private void setupListener(ObservableList<Interview> interviews) {
        if(this.interviews != null && listChangeListener != null) {
            this.interviews.removeListener(listChangeListener);
        }
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
}

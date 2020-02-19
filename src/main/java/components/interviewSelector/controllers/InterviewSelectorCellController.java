package components.interviewSelector.controllers;

import components.interviewSelector.models.Interview;
import application.configuration.Configuration;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class InterviewSelectorCellController implements Initializable {

    @FXML Label name;
    @FXML ImageView pictureView;
    @FXML MenuButton optionsMenu;

    protected Interview interview;
    private boolean shouldRemoveMenuButtonVisibility;
    private InterviewSelectorCommandFactory commandFactory;

    public InterviewSelectorCellController(Interview interview, InterviewSelectorCommandFactory commandFactory) {
        this.interview = interview;
        this.commandFactory = commandFactory;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage("category.png"));

        name.setText(interview.getTitle());

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> { commandFactory.deleteInterview(interview).execute(); });
        optionsMenu.getItems().add(deleteButton);

        optionsMenu.setVisible(false);
        optionsMenu.onHiddenProperty().addListener((observableValue, eventEventHandler, t1) -> {
            if(shouldRemoveMenuButtonVisibility) { shouldRemoveMenuButtonVisibility = false; optionsMenu.setVisible(false);}
        });
    }

    public void setOnHover(boolean YoN) {
        if(optionsMenu.isShowing())
            shouldRemoveMenuButtonVisibility = true;
        else
            optionsMenu.setVisible(YoN);
    }

    public boolean getOnHover() { return optionsMenu.isVisible(); }


}

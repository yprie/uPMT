package components.interviewSelector.controllers;

import application.configuration.Configuration;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.modelisationSpace.controllers.ModelisationSpaceController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import models.Interview;
import utils.ResourceLoader;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InterviewSelectorCellController implements Initializable {

    @FXML Label name;
    @FXML ImageView pictureView;
    @FXML MenuButton optionsMenu;

    protected Interview interview;
    private boolean shouldRemoveMenuButtonVisibility;
    private InterviewSelectorCommandFactory commandFactory;
    private ModelisationSpaceController modelisationSpaceController;

    public InterviewSelectorCellController(Interview interview, InterviewSelectorCommandFactory commandFactory, ModelisationSpaceController modelisationSpaceController) {
        this.interview = interview;
        this.commandFactory = commandFactory;
        this.modelisationSpaceController = modelisationSpaceController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage("category.png"));
        name.setText(interview.getTitle());

        interview.titleProperty().addListener((observableValue, s, t1) -> name.setText(t1));

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        MenuItem editButton = new MenuItem(Configuration.langBundle.getString("edit"));
        MenuItem printAsButton = new MenuItem("export as png");

        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Configuration.langBundle.getString("confirmation_dialog"));
            alert.setHeaderText(Configuration.langBundle.getString("delete_interview"));
            alert.setContentText(Configuration.langBundle.getString("continue_alert"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                commandFactory.deleteInterview(interview).execute();
            }
        });
        editButton.setOnAction(actionEvent -> { commandFactory.modifyInterview(interview).execute(); });
        printAsButton.setOnAction(actionEvent -> { commandFactory.printAsInterview(interview, modelisationSpaceController).execute(); });

        optionsMenu.getItems().add(editButton);
        optionsMenu.getItems().add(deleteButton);
        optionsMenu.getItems().add(printAsButton);

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

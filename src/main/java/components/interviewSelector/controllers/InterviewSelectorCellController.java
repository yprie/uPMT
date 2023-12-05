package components.interviewSelector.controllers;

import application.configuration.Configuration;
import components.interviewSelector.InterviewSelectorCell;
import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
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
    InterviewSelectorCell interviewSelectorCell;
    protected Interview interview;
    private boolean shouldRemoveMenuButtonVisibility;
    private InterviewSelectorCommandFactory commandFactory;

    public InterviewSelectorCellController(Interview interview, InterviewSelectorCell interviewSelectorCell, InterviewSelectorCommandFactory commandFactory) {
        this.interview = interview;
        this.commandFactory = commandFactory;
        this.interviewSelectorCell=interviewSelectorCell;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureView.setImage(ResourceLoader.loadImage("interview-"+interview.getColor()+".png"));
        name.setText(interview.getTitle());

        interview.titleProperty().addListener((observableValue, s, t1) -> name.setText(t1));

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        MenuItem editButton = new MenuItem(Configuration.langBundle.getString("edit"));
//        updateColor();
        //ADDING COLOR
        Menu changeColor = new Menu(Configuration.langBundle.getString("change_color"));

        MenuItem white = new MenuItem("    ");
        white.setStyle("-fx-background-color: #ffffff;\n");
        white.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "ffffff",this).execute());
        changeColor.getItems().add(white);

        MenuItem brown = new MenuItem("    ");
        brown.setStyle("-fx-background-color: #b97a57;\n");
        brown.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "b97a57",this).execute());
        changeColor.getItems().add(brown);

        MenuItem pink = new MenuItem("    ");
        pink.setStyle("-fx-background-color: #ffaec9;\n");
        pink.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "ffaec9",this).execute());

        changeColor.getItems().add(pink);

        MenuItem yellow = new MenuItem("    ");
        yellow.setStyle("-fx-background-color: #ffc90e;\n");
        yellow.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "ffc90e",this).execute());
        changeColor.getItems().add(yellow);

        MenuItem green = new MenuItem("    ");
        green.setStyle("-fx-background-color: #b5e61d;\n");
        green.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "b5e61d",this).execute());
        changeColor.getItems().add(green);

        MenuItem blue = new MenuItem("    ");
        blue.setStyle("-fx-background-color: #7092be;\n");
        blue.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "7092be",this).execute());
        changeColor.getItems().add(blue);

        MenuItem purple = new MenuItem("    ");
        purple.setStyle("-fx-background-color: #8671cd;\n");
        purple.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "8671cd",this).execute());
        changeColor.getItems().add(purple);

        MenuItem red = new MenuItem("    ");
        red.setStyle("-fx-background-color: #f15252;\n");
        red.setOnAction(actionEvent -> commandFactory.colorCommand(interview, "f15252",this).execute());

        changeColor.getItems().add(red);

        //END ADDING
        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(Configuration.langBundle.getString("confirmation_dialog"));
            alert.setHeaderText(Configuration.langBundle.getString("delete_interview"));
            alert.setContentText(Configuration.langBundle.getString("continue_alert"));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                commandFactory.deleteInterview(interview).execute();
            }
        });
        editButton.setOnAction(actionEvent -> { commandFactory.modifyInterview(interview).execute(); });

        optionsMenu.getItems().add(editButton);
        optionsMenu.getItems().add(deleteButton);
        optionsMenu.getItems().add(changeColor);//change
        optionsMenu.setVisible(false);
        optionsMenu.onHiddenProperty().addListener((observableValue, eventEventHandler, t1) -> {
            if(shouldRemoveMenuButtonVisibility) { shouldRemoveMenuButtonVisibility = false; optionsMenu.setVisible(false);}
        });

    }
    public void updateColor() {
       this.pictureView.setImage(ResourceLoader.loadImage("interview-"+interview.getColor()+".png"));
//        this.interviewSelectorCell.setStyle("-fx-text-fill: black;\n");

    }
    public void setOnHover(boolean YoN) {
        if(optionsMenu.isShowing())
            shouldRemoveMenuButtonVisibility = true;
        else
            optionsMenu.setVisible(YoN);
    }

    public boolean getOnHover() { return optionsMenu.isVisible(); }


}

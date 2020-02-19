package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.Section;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.models.Justification;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.VBox.VBoxModelController;
import utils.modelControllers.VBox.VBoxModelUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationCell extends VBoxModelController<Descripteme> implements Initializable {

    @FXML
    private Label text;

    @FXML
    private Button removeButton;

    @FXML
    private Button duplicateButton;

    private JustificationCommandFactory factory;
    private Descripteme descripteme;

    public JustificationCell(Descripteme d, JustificationCommandFactory factory) {
        this.descripteme = d;
        this.factory = factory;
    }

    public static Node create(JustificationCell controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/Justification/JustificationCell.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Text init
        text.setText(descripteme.getSelection());

        //Remove init
        removeButton.setOnAction(actionEvent -> {
            factory.removeDescripteme(descripteme).execute();
        });

        duplicateButton.setOnAction(actionEvent -> {
            factory.duplicateDescripteme(descripteme).execute();
        });
    }

    @Override
    public Descripteme getModel() {
        return descripteme;
    }

    @Override
    public void onUpdate(VBoxModelUpdate update) {

    }

    @Override
    public void onUnmount() {

    }


}

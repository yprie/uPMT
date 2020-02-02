package components.modelisationSpace.controllers;

import components.interviewPanel.Models.Descripteme;
import components.interviewPanel.Models.InterviewText;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.justification.models.Justification;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;
import utils.scrollOnDragPane.ScrollOnDragPane;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    @FXML
    private BorderPane centerPane;

    public ModelisationSpaceController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modelisationSpace/ModelisationSpace.fxml"));
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
        super.initialize(location, resources);
        //fake_view.setImage(ResourceLoader.loadImage("fake_modelisation.png"));

        //Fake content
        InterviewText it = new InterviewText("Ceci est un texte d'interview !");

        Justification j1 = new Justification();
        j1.addDescripteme(new Descripteme(it, 0, 5));
        j1.addDescripteme(new Descripteme(it, 5, 10));

        Justification j2 = new Justification();
        j2.addDescripteme(new Descripteme(it, 5, 10));
        j2.addDescripteme(new Descripteme(it, 10, 16));

        JustificationController c1 = new JustificationController(j1);
        JustificationController c2 = new JustificationController(j2);

        this.centerPane.setLeft(JustificationController.createJustificationArea(c1));
        this.centerPane.setCenter(JustificationController.createJustificationArea(c2));
    }
}


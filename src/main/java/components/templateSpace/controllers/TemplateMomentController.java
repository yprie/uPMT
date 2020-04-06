package components.templateSpace.controllers;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import models.TemplateMoment;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TemplateMomentController implements Initializable {

    private TemplateMoment templateMoment;

    private @FXML BorderPane container;
    private @FXML Label name;

    public TemplateMomentController(TemplateMoment templateMoment) {
        this.templateMoment = templateMoment;
    }

    public static Node createTemplateMoment(TemplateMomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/TemplateSpace/TemplateMoment.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.textProperty().bind(templateMoment.nameProperty());

        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        container.setOnDragDetected(event -> {
            Dragboard db = container.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(TemplateMoment.format, 0);
            DragStore.setDraggable(templateMoment);
            db.setContent(content);
            event.consume();
        });
    }
}

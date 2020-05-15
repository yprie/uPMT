package components.templateSpace.controllers;

import components.templateSpace.emptyTemplateMoment.EmptyTemplateMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TemplateSpaceController extends VBox implements Initializable {

    private TemplateMomentController emptyTemplateMomentController;
    private @FXML HBox momentsBox;

    public TemplateSpaceController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TemplateSpace/TemplateSpace.fxml"));
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
        EmptyTemplateMoment m1 = new EmptyTemplateMoment();
        emptyTemplateMomentController = new TemplateMomentController(m1);
        momentsBox.getChildren().add(TemplateMomentController.createTemplateMoment(emptyTemplateMomentController));
    }
}

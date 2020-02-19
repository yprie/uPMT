package components.modelisationSpace.controllers;

import components.modelisationSpace.moment.controllers.RootMomentController;
import components.modelisationSpace.moment.model.RootMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import javafx.scene.layout.BorderPane;
import utils.scrollOnDragPane.ScrollOnDragPane;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    private RootMomentController rmController;
    private  @FXML BorderPane pane;

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
    }

    public void setRootMoment(RootMoment m) {
        //Set new moment
        clearSpace();
        if(m != null) {
            rmController = new RootMomentController(m);
            pane.setCenter(RootMomentController.createRootMoment(rmController));
        }
    }

    public void clearSpace() {
        if(rmController != null)
            rmController.unmount();
        pane.setCenter(null);
    }
}


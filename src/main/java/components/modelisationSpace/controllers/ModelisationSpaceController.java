package components.modelisationSpace.controllers;

import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.moment.controllers.RootMomentController;
import models.RootMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import utils.scrollOnDragPane.ScrollOnDragPane;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    private  @FXML ImageView fake_view;
    private @FXML AnchorPane mainAnchorPane;
    private ScrollPaneCommandFactory paneCmdFactory;
    private RootMomentController rmController;

    private  @FXML AnchorPane pane;
    private @FXML ScrollPane superPane;

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
        paneCmdFactory = new ScrollPaneCommandFactory(superPane);
    }

    public void setRootMoment(RootMoment m) {
        //Set new moment
        clearSpace();
        if(m != null) {
            rmController = new RootMomentController(m, paneCmdFactory);
            superPane.setContent(RootMomentController.createRootMoment(rmController));
        }
    }

    public void clearSpace() {
        if(rmController != null)
            rmController.unmount();
        superPane.setContent(null);
    }
}

package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import components.modelisationSpace.moment.model.Moment;
import components.modelisationSpace.moment.model.RootMoment;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import utils.modelControllers.HBox.HBoxModel;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootMomentController implements Initializable {

    private RootMoment moment;
    private MomentCommandFactory childCmdFactory;

    private @FXML BorderPane momentsSpace;
    private HBoxModel<Moment, MomentController> momentsHBox;


    private ListChangeListener<Moment> childChangeListener = change -> {
        while(change.next()) {
            for (Moment remitem : change.getRemoved()) {
                momentsHBox.remove(remitem);
            }
            for (Moment additem : change.getAddedSubList()) {
                momentsHBox.add(change.getTo()-1, additem);
            }
        }
    };

    public RootMomentController(RootMoment m) {
        this.moment = m;
        this.childCmdFactory = new MomentCommandFactory(moment);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        momentsHBox = new HBoxModel<Moment, MomentController>(
                (m -> new MomentController(m, childCmdFactory)),
                MomentController::createMoment);

        for(Moment m : moment.momentsProperty()) {
            momentsHBox.add(m);
        }
        momentsSpace.setCenter(momentsHBox);
        moment.momentsProperty().addListener(childChangeListener);
    }

    public static Node createRootMoment(RootMomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/moment/RootMoment.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void unbindMomentListener() {
        moment.momentsProperty().removeListener(childChangeListener);
    }
}

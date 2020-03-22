package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import models.Moment;
import models.RootMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import utils.modelControllers.ListView.ListView;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootMomentController implements Initializable {

    private RootMoment moment;
    private MomentCommandFactory childCmdFactory;
    private ScrollPaneCommandFactory paneCmdFactory;

    private @FXML BorderPane momentsSpace;
    private @FXML HBox childrenBox;
    private @FXML HBox modelMomentBox;
    private ListView<Moment, MomentController> momentsHBox;



    public RootMomentController(RootMoment m, ScrollPaneCommandFactory paneCmdFactory) {
        this.moment = m;
        this.childCmdFactory = new MomentCommandFactory(moment);
        this.paneCmdFactory = paneCmdFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        momentsHBox = new ListView<>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment,
                childrenBox);

        Moment modelMoment = new Moment(Configuration.langBundle.getString("new_moment"));
        modelMomentBox.getChildren().add(ModelMomentController.createMoment(new ModelMomentController(modelMoment, childCmdFactory, paneCmdFactory)));
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

    public void unmount() {
        momentsHBox.onUnmount();
    }
}

package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import models.Moment;
import models.RootMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import utils.modelControllers.ListView.ListView;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootMomentController implements Initializable {

    private RootMoment moment;
    private MomentCommandFactory childCmdFactory;
    private ScrollPaneCommandFactory paneCmdFactory;

    private @FXML HBox childrenBox;
    private ListView<Moment, MomentController> momentsHBox;


    public RootMomentController(RootMoment m, ScrollPaneCommandFactory paneCmdFactory, ModelisationSpaceHookNotifier hooksNotifier) {
        this.moment = m;
        this.childCmdFactory = new MomentCommandFactory(hooksNotifier, moment);
        this.paneCmdFactory = paneCmdFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        momentsHBox = new ListView<>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment,
                childrenBox);
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

    public boolean hasAtLeastOneChildMoment() { return moment.momentsProperty().size() > 0;}
    public void addMoment(Moment moment) {
        childCmdFactory.addSiblingCommand(moment).execute();
    }
}

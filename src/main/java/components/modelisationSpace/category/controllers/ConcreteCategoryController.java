package components.modelisationSpace.category.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.justification.models.Justification;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConcreteCategoryController extends ListViewController<ConcreteCategory> implements Initializable {

    private ScrollPaneCommandFactory paneCommandFactory;
    private ConcreteCategory category;
    private JustificationController justificationController;

    @FXML private BorderPane container;
    @FXML private Label name;

    public ConcreteCategoryController(ConcreteCategory c, ScrollPaneCommandFactory paneCommandFactory) {
        this.category = c;
        this.paneCommandFactory = paneCommandFactory;
        this.justificationController = new JustificationController(new Justification());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.textProperty().bind(category.nameProperty());
        container.setCenter(JustificationController.createJustificationArea(justificationController));
    }

    public static Node create(ConcreteCategoryController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/concreteCategory/ConcreteCategory.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ConcreteCategory getModel() {
        return category;
    }

    @Override
    public void onMount() {
        Timeline viewFocus = new Timeline(new KeyFrame(Duration.seconds(0.1),
                (EventHandler<ActionEvent>) event -> { paneCommandFactory.scrollToNode(container).execute(); }));
        viewFocus.play();
    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {

    }


}

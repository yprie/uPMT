package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.appCommands.RemoveDescriptemeCommand;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationCell extends ListViewController<Descripteme> implements Initializable {

    @FXML
    private Label text;

    @FXML
    private MenuButton menuButton;

    @FXML BorderPane container;

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

        //Actions
        MenuItem removeButton = new MenuItem(Configuration.langBundle.getString("delete"));
        removeButton.setOnAction(actionEvent -> {
            factory.removeDescripteme(descripteme).execute();
        });
        menuButton.getItems().add(removeButton);

        MenuItem duplicateButton = new MenuItem(Configuration.langBundle.getString("duplicate"));
        duplicateButton.setOnAction(actionEvent -> {
            factory.duplicateDescripteme(descripteme).execute();
        });
        menuButton.getItems().add(duplicateButton);

        setupDnd();
    }

    private void setupDnd() {
        container.setOnDragDetected(mouseEvent -> {
            Dragboard db = container.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(descripteme.getDataFormat(), 0);
            DragStore.setDraggable(descripteme);
            db.setContent(content);

            container.setStyle("-fx-background-color: #bdc3c7;");
            mouseEvent.consume();
        });

        container.setOnDragDone(dragEvent -> {
            container.setStyle("-fx-background-color: transparent;");
            if (dragEvent.getTransferMode() == TransferMode.MOVE) {
                RemoveDescriptemeCommand c = factory.removeDescripteme(DragStore.getDraggable());
                c.isNotUserAction();
                c.execute();
            }
            dragEvent.consume();
        });
    }

    @Override
    public Descripteme getModel() {
        return descripteme;
    }

    @Override
    public void onMount() {

    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {

    }
}

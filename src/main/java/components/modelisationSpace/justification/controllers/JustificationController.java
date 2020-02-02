package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.models.Justification;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationController implements Initializable{

    private Justification justification;
    private JustificationCommandFactory factory;

    @FXML
    private ListView<Descripteme> listView;

    public JustificationController(Justification j) {
        this.justification = j;
        this.factory = new JustificationCommandFactory(justification);
    }

    public static Node createJustificationArea(JustificationController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/Justification/Justification.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listView.setCellFactory(descriptemeListView -> { return new JustificationCell(factory); });
        listView.setItems(justification.descriptemesProperty());
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {

        JustificationController selfController = this;

        listView.setOnDragOver(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        listView.setOnDragDropped(dragEvent -> {
            //Si on récupère depuis une JustificationCell
            if(dragEvent.getGestureSource() instanceof JustificationCell){
                JustificationCell source = (JustificationCell)dragEvent.getGestureSource();
                source.getCommandFactory().moveDescripteme(DragStore.getDraggable(), selfController.factory).execute();
            }

            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });
    }

}

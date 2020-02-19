package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.models.Justification;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.VBox.VBoxModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationController implements Initializable{

    //Controller logical elements
    private Justification justification;
    private JustificationCommandFactory cmdFactory;


    //Graphics elements
    private VBoxModel<Descripteme, JustificationCell> descriptemesVBox;
    @FXML private VBox container;
    @FXML private HBox descriptemeDndZone;

    //update listener
    private ListChangeListener<Descripteme> changeListener = change -> {
        while(change.next()) {
            for (Descripteme remitem : change.getRemoved()) {
                descriptemesVBox.remove(remitem);
            }
            for (Descripteme additem : change.getAddedSubList()) {
                descriptemesVBox.add(additem);
            }
        }
    };

    public JustificationController(Justification j) {
        this.justification = j;
        this.cmdFactory = new JustificationCommandFactory(justification);
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
        //Adding descripteme VBox
        descriptemesVBox = new VBoxModel<>(
                (descripteme -> new JustificationCell(descripteme, cmdFactory)),
                JustificationCell::create
        );
        container.getChildren().add(0, descriptemesVBox);

        //Setting up drag and drop operation
        setupDescriptemeDND();

        //listeners binding
        justification.descriptemesProperty().addListener(changeListener);

    }

    private void setupDescriptemeDND() {

        descriptemeDndZone.setOnDragOver(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        descriptemeDndZone.setOnDragDropped(dragEvent -> {
            if(DragStore.getDraggable().isDraggable() && DragStore.getDraggable().getDataFormat() == Descripteme.format){
                cmdFactory.addDescripteme(DragStore.getDraggable()).execute();
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });
    }

}

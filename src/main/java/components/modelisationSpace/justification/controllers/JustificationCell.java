package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.justification.Section;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.models.Justification;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import utils.dragAndDrop.DragStore;

import java.io.IOException;

public class JustificationCell extends ListCell<Descripteme> {

    @FXML
    private Label text;

    @FXML
    private Button removeButton;

    @FXML
    private Button duplicateButton;

    private JustificationCommandFactory factory;
    private Descripteme descripteme;

    public JustificationCell(JustificationCommandFactory factory) {
        this.factory = factory;
    }

    @Override
    protected void updateItem(Descripteme d, boolean empty) {
        super.updateItem(d, empty);

        if(empty || d == null) {
            //setText(null);
            setGraphic(null);
            this.descripteme = null;
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/modelisationSpace/Justification/JustificationCell.fxml"));
                loader.setController(this);
                loader.setResources(Configuration.langBundle);
                setGraphic(loader.load());
                this.descripteme = d;
                init();
            } catch (IOException e) {
                e.printStackTrace();
                setGraphic(null);
            }}
    }

    //Init the appearance and controls of the component
    private void init() {
        //Text init
        text.setText(descripteme.getSelection());

        //Remove init
        removeButton.setOnAction(actionEvent -> {
            factory.removeDescripteme(descripteme).execute();
        });

        duplicateButton.setOnAction(actionEvent -> {
            factory.duplicateDescripteme(descripteme).execute();
        });

        //Drag and drop setup
        setupDragAndDrop();
    }


    private void setupDragAndDrop() {
        JustificationCell selfCell = this;

        selfCell.setOnDragDetected(mouseEvent -> {
            if(descripteme.isDraggable()) {
                Dragboard db = selfCell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.put(descripteme.getDataFormat(), 0);
                DragStore.setDraggable(descripteme);
                db.setContent(content);
            }
            mouseEvent.consume();
        });

        selfCell.setOnDragExited(dragEvent -> {
            dragEvent.consume();
        });

        selfCell.setOnDragOver(dragEvent -> {
            //Check for a descripteme different from the original one
            if( DragStore.getDraggable().getDataFormat() == Descripteme.format &&
                DragStore.getDraggable() != selfCell.getDescripteme()
            ) {
                Section dragSection = (dragEvent.getY() <= selfCell.getHeight() / 2) ? Section.top : Section.bottom;
                //TODO -- Apply CSS style
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        selfCell.setOnDragDropped(dragEvent -> {
            Section dragSection = (dragEvent.getY() <= selfCell.getHeight() / 2) ? Section.top : Section.bottom;

            //Si on récupère depuis une JustificationCell
            if(dragEvent.getGestureSource() instanceof JustificationCell){
                JustificationCell source = (JustificationCell)dragEvent.getGestureSource();
                source.getCommandFactory().moveDescripteme(DragStore.getDraggable(), selfCell.getCommandFactory(), selfCell.getDescripteme(), dragSection).execute();
            }

            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });
    }

    private Descripteme getDescripteme() {
        return descripteme;
    }

    public JustificationCommandFactory getCommandFactory() { return factory; }

}

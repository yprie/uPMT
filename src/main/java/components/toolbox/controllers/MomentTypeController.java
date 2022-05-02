package components.toolbox.controllers;

import application.configuration.Configuration;
import components.toolbox.models.MomentType;
import components.toolbox.models.SchemaMomentType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import models.Moment;
import models.SchemaElement;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MomentTypeController implements Initializable {
    private @FXML BorderPane momentTypeBorderPane;
    private @FXML Label momentTypeLabel;
    private Moment momentType;
    private SchemaElement schemaMomentType;

    public MomentTypeController(Moment moment) {
        this.schemaMomentType = new SchemaMomentType(moment.getName(), this);
        this.momentType = new MomentType(moment,this);
    }

    public static Node createMomentTypeController(MomentTypeController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/Toolbox/components/MomentType.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupDragAndDrop() {
       momentTypeBorderPane.setOnDragDetected(event -> {
           Moment newMomentType = new MomentType(this.momentType, this);
           Dragboard db = momentTypeBorderPane.startDragAndDrop(TransferMode.MOVE);
           ClipboardContent content = new ClipboardContent();
           content.put(newMomentType.getDataFormat(), 0);
           DragStore.setDraggable(newMomentType);
           db.setContent(content);
       });

       momentTypeBorderPane.setOnDragDone(event -> {
           event.consume();
       });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       this.momentTypeLabel.setText(this.momentType.getName());
       this.momentTypeBorderPane.setStyle("-fx-background-color: #"+this.momentType.getColor()+";");
       setupDragAndDrop();
    }

    public boolean exists(Moment moment) {
        if (moment.getName().equals(this.momentType.getName())) {
            return true;
        }
        return false;
    }

    public void rename(String name) {
        this.momentTypeLabel.setText(name);
        this.momentType.setName(name);
        this.schemaMomentType.setName(name);
    }

    public Moment getMomentType() {
        return momentType;
    }

    public SchemaElement getSchemaMomentType() {
        return schemaMomentType;
    }
}

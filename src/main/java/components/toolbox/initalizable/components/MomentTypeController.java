package components.toolbox.initalizable.components;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import models.Moment;
import javafx.scene.control.Label;
import models.MomentType;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MomentTypeController implements Initializable {
    private MomentType momentType;
    private @FXML BorderPane momentTypeBorderPane;
    private @FXML Label momentTypeLabel;

    public MomentTypeController(Moment moment) {
        this.momentType = new MomentType(moment);
    }

    public static Node createMomentTypeController(MomentTypeController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/ToolBox/components/MomentType.fxml"));
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
           Moment newMomentType = new MomentType(this.momentType);
           Dragboard db = momentTypeBorderPane.startDragAndDrop(TransferMode.MOVE);
           ClipboardContent content = new ClipboardContent();
           content.put(newMomentType.getDataFormat(), 0);
           DragStore.setDraggable(newMomentType);
           db.setContent(content);
       });

       momentTypeBorderPane.setOnDragDone(event -> {
           System.out.println("consume");
           event.consume();
       });
   }

   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
       this.momentTypeLabel.setText(this.momentType.getName());
       this.momentTypeBorderPane.setStyle("-fx-background-color: #"+this.momentType.getColor()+";");
       setupDragAndDrop();
   }

    public MomentType getMomentType() {
        return momentType;
    }
}

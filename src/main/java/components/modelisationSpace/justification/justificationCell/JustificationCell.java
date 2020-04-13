package components.modelisationSpace.justification.justificationCell;

import application.configuration.Configuration;
import javafx.beans.value.ChangeListener;
import models.Descripteme;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import components.modelisationSpace.justification.appCommands.RemoveDescriptemeCommand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationCell extends ListViewController<Descripteme> implements Initializable {

    @FXML private Label text;
    @FXML private MenuButton menuButton;
    @FXML BorderPane container;
    @FXML ImageView descriptemeDndLogo;

    private JustificationCommandFactory factory;
    private Descripteme descripteme;

    private ChangeListener<String> onDescriptemeChange = (observableValue, o, t1) -> {
        updateToolTip();
    };

    public JustificationCell(Descripteme d, JustificationCommandFactory factory) {
        this.descripteme = d;
        this.factory = factory;
    }

    public static Node create(JustificationCell controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/Justification/JustificationCell/JustificationCell.fxml"));
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
        text.textProperty().bind(descripteme.getSelectionProperty());

/*        ShiftController leftShiftController = new ShiftController(descripteme, factory, "left");
        moveLeft.getChildren().add(ShiftController.createShiftController(leftShiftController));

        ShiftController rightShiftController = new ShiftController(descripteme, factory, "right");
        moveRight.getChildren().add(ShiftController.createShiftController(rightShiftController));*/


        //Actions
        MenuItem modifyButton = new MenuItem(Configuration.langBundle.getString("modify"));
        modifyButton.setOnAction(actionEvent -> {
            EditJustificationCell.edit(descripteme, menuButton.getScene().getWindow());
        });
        menuButton.getItems().add(modifyButton);

        MenuItem duplicateButton = new MenuItem(Configuration.langBundle.getString("duplicate"));
        duplicateButton.setOnAction(actionEvent -> {
            factory.duplicateDescripteme(descripteme).execute();
        });
        menuButton.getItems().add(duplicateButton);

        MenuItem removeButton = new MenuItem(Configuration.langBundle.getString("delete"));
        removeButton.setOnAction(actionEvent -> {
            factory.removeDescripteme(descripteme).execute();
        });
        menuButton.getItems().add(removeButton);

        //Descripteme tooltip
        updateToolTip();

        //Duplicate shortcut
        text.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.isShiftDown()) {
                if(mouseEvent.getButton() == MouseButton.PRIMARY)
                    factory.duplicateDescripteme(descripteme).execute();
                else if(mouseEvent.getButton() == MouseButton.SECONDARY)
                    factory.removeDescripteme(descripteme).execute();
            }
        });

        setupDnd();

        //text.setOnMouseEntered(event -> text.setStyle("-fx-cursor: move;"));
    }

    private void setupDnd() {
        container.setOnDragDetected(mouseEvent -> {
            Dragboard db;
            Descripteme d;
            if(mouseEvent.isShiftDown()) {
                db = container.startDragAndDrop(TransferMode.COPY);
                d = descripteme.duplicate();
            }
            else {
                db = container.startDragAndDrop(TransferMode.MOVE);
                d = descripteme;
            }

            ClipboardContent content = new ClipboardContent();
            content.put(descripteme.getDataFormat(), 0);
            DragStore.setDraggable(d);
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
        descripteme.getSelectionProperty().addListener(onDescriptemeChange);
    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {
        descripteme.getSelectionProperty().removeListener(onDescriptemeChange);
    }

    /*
    private void modifyDescripteme() {

    }
    */

    public void updateToolTip() {
        Tooltip.install(text, new Tooltip(descripteme.getSelection()));
    }


}

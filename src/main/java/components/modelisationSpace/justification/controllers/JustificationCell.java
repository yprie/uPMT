package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.HBox;
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
import javafx.scene.text.Text;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JustificationCell extends ListViewController<Descripteme> implements Initializable {

    @FXML private HBox moveLeft;
    @FXML private HBox moveRight;
    @FXML private Text text;
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
        //text.setText(descripteme.getSelection());
        text.wrappingWidthProperty().set(200);
        /*text.setWrapText(false);*/
        text.textProperty().bind(descripteme.getSelectionProperty());
        text.underlineProperty().bind(descripteme.getEmphasizeProperty());

        ShiftController leftShiftController = new ShiftController(descripteme, factory, "left");
        moveLeft.getChildren().add(ShiftController.createShiftController(leftShiftController));

        ShiftController rightShiftController = new ShiftController(descripteme, factory, "right");
        moveRight.getChildren().add(ShiftController.createShiftController(rightShiftController));


        //Actions
        MenuItem duplicateButton = new MenuItem(Configuration.langBundle.getString("duplicate"));
        duplicateButton.setOnAction(actionEvent -> {
            factory.duplicateDescripteme(descripteme).execute();
        });
        menuButton.getItems().add(duplicateButton);

        /*MenuItem modifyButton = new MenuItem(Configuration.langBundle.getString("modify"));
        modifyButton.setOnAction(actionEvent -> {
            this.modifyDescripteme();
        });
        menuButton.getItems().add(modifyButton);
        */

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

        text.setOnMouseEntered(event -> text.setStyle("-fx-cursor: move;"));
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

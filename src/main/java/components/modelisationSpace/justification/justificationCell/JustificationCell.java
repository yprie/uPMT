package components.modelisationSpace.justification.justificationCell;

import application.configuration.AppSettings;
import application.configuration.Configuration;
import javafx.application.Platform;
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
import org.fxmisc.richtext.event.MouseStationaryEvent;
import org.fxmisc.richtext.event.MouseStationaryHelper;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class JustificationCell extends ListViewController<Descripteme> implements Initializable {

    @FXML private Label text;
    @FXML private MenuButton menuButton;
    @FXML BorderPane container;

    private JustificationCommandFactory factory;
    private Descripteme descripteme;

    private MenuItem revealButton;
    MouseStationaryHelper mouseStationaryHelper;

    private ChangeListener<String> onDescriptemeChange = (observableValue, o, t1) -> updateToolTip();

    private ChangeListener autoScrollWhenRevealListener = (observableValue, oldValue, newValue) -> {
        if (AppSettings.autoScrollWhenReveal.get()) {
            mouseStationaryHelper.install(Duration.ofMillis(AppSettings.delayRevealDescripteme)); // set here the duration to wait before it scrolls to the descripteme in the interview
        } else {
            mouseStationaryHelper.uninstall();
        }

        revealButton.setVisible(!AppSettings.autoScrollWhenReveal.get());
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
        text.underlineProperty().bind(descripteme.getEmphasizeProperty());

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
        duplicateButton.setOnAction(actionEvent -> factory.duplicateDescripteme(descripteme).execute());
        menuButton.getItems().add(duplicateButton);

        MenuItem removeButton = new MenuItem(Configuration.langBundle.getString("delete"));
        removeButton.setOnAction(actionEvent -> factory.removeDescripteme(descripteme).execute());
        menuButton.getItems().add(removeButton);

        revealButton = new MenuItem(Configuration.langBundle.getString("reveal"));
        revealButton.setOnAction(actionEvent -> descripteme.setTriggerScrollReveal(true));
        revealButton.setVisible(!AppSettings.autoScrollWhenReveal.get());
        menuButton.getItems().add(revealButton);

        //Descripteme tooltip
        updateToolTip();

        //Duplicate shortcut
        text.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.isShiftDown()) {
                if(mouseEvent.getButton() == MouseButton.SECONDARY || mouseEvent.isControlDown())
                    factory.removeDescripteme(descripteme).execute();
                else if(mouseEvent.getButton() == MouseButton.PRIMARY)
                    factory.duplicateDescripteme(descripteme).execute();
            }
        });

        text.setOnMouseEntered(event -> descripteme.setRevealed(true));

        text.addEventHandler(MouseStationaryEvent.MOUSE_STATIONARY_BEGIN, event -> {
            descripteme.setTriggerScrollReveal(true);
        });
        mouseStationaryHelper = new MouseStationaryHelper(text);
        if (AppSettings.autoScrollWhenReveal.get()) {
            mouseStationaryHelper.install(Duration.ofMillis(AppSettings.delayRevealDescripteme)); // set here the duration to wait before it scrolls to the descripteme in the interview
        }

        text.setOnMouseExited(event -> descripteme.setRevealed(false));

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
                Platform.runLater(c::execute);
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
        AppSettings.autoScrollWhenReveal.addListener(autoScrollWhenRevealListener);
    }

    @Override
    public void onUpdate(ListViewUpdate update) {

    }

    @Override
    public void onUnmount() {
        descripteme.getSelectionProperty().removeListener(onDescriptemeChange);
        AppSettings.autoScrollWhenReveal.removeListener(autoScrollWhenRevealListener);
    }

    /*
    private void modifyDescripteme() {

    }
    */

    public void updateToolTip() {
        Tooltip.install(text, new Tooltip(descripteme.getSelection()));
    }

}

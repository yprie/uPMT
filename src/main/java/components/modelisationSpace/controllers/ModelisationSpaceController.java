package components.modelisationSpace.controllers;

import application.configuration.AppSettings;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.hooks.ModelisationSpaceHook;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.moment.controllers.RootMomentController;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import models.RootMoment;
import models.TemplateMoment;
import utils.dragAndDrop.DragStore;
import utils.scrollOnDragPane.ScrollOnDragPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    private ScrollPaneCommandFactory paneCmdFactory;
    private RootMomentController rmController;

    private  @FXML AnchorPane pane;
    private @FXML ScrollOnDragPane superPane;

    AnchorPane anchorPane = new AnchorPane(); // Container

    private ModelisationSpaceHook hooks;
    private ModelisationSpaceHookNotifier hooksNotifier;

    public ModelisationSpaceController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modelisationSpace/ModelisationSpace.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        hooks = new ModelisationSpaceHook();
        hooksNotifier = new ModelisationSpaceHookNotifier(hooks);
    }

    public void TakeSnapshot(){
        try {
            System.out.println("Test de la méthode snapshot");
            WritableImage image = this.anchorPane.snapshot(new SnapshotParameters(), null);
            File file = new File("C:\\Users\\esteb\\.upmt\\test");

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        paneCmdFactory = new ScrollPaneCommandFactory(superPane);
        setupDragAndDrop();
    }

    public void setRootMoment(RootMoment m) {
        //Set new moment
        clearSpace();
        if(m != null) {
            rmController = new RootMomentController(m, paneCmdFactory, hooksNotifier);
            anchorPane.getChildren().clear();
            anchorPane.getChildren().add(RootMomentController.createRootMoment(rmController));
            superPane.setContent(anchorPane);
            double r = AppSettings.zoomLevelProperty.getValue() * 0.01;
            anchorPane.getTransforms().setAll(new Scale(r, r,0, 0));
            AppSettings.zoomLevelProperty.addListener((l) -> {
                double ratio = AppSettings.zoomLevelProperty.getValue() * 0.01;
                anchorPane.getTransforms().setAll(new Scale(ratio, ratio,0, 0));
            });
        }
    }

    public void clearSpace() {
        if(rmController != null)
            rmController.unmount();
        superPane.setContent(null);
    }

    public ModelisationSpaceHook getHooks() { return hooks; }

    private void setupDragAndDrop() {
        superPane.setOnDragOverHook((e) -> {
            if(
                !rmController.hasAtLeastOneChildMoment()
                && DragStore.getDraggable().isDraggable()
                && !e.isAccepted()
                && DragStore.getDraggable().getDataFormat() == TemplateMoment.format
            ) {
                e.acceptTransferModes(TransferMode.COPY);
            }
        });

        superPane.setOnDragDroppedHook(e -> {
            System.out.println("DataFormat : " + DragStore.getDraggable().getDataFormat().toString());
            if(
                !rmController.hasAtLeastOneChildMoment()
                && e.isAccepted()
                && DragStore.getDraggable().getDataFormat() == TemplateMoment.format
            ) {
                TemplateMoment t = DragStore.getDraggable();
                rmController.addMoment(t.createConcreteMoment());
            }
        });
    }
}

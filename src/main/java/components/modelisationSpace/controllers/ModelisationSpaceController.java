package components.modelisationSpace.controllers;

import application.configuration.AppSettings;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.hooks.ModelisationSpaceHook;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.moment.controllers.RootMomentController;
import javafx.scene.input.TransferMode;
import javafx.scene.transform.Scale;
import models.RootMoment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;

import javafx.scene.layout.AnchorPane;
import models.TemplateMoment;
import utils.dragAndDrop.DragStore;
import utils.scrollOnDragPane.ScrollOnDragPane;
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

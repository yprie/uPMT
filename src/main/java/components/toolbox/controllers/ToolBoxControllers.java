package components.toolbox.controllers;

import application.configuration.Configuration;
import components.templateSpace.controllers.TemplateSpaceController;
import components.toolbox.appCommand.AddSchemaMomentTypeCommand;
import components.toolbox.appCommand.RemoveSchemaMomentTypeCommand;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.*;
import utils.GlobalVariables;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class ToolBoxControllers extends HBox implements Initializable {
    private @FXML
    HBox bodyToolBox;
    private @FXML
    Button addMomentTypeFolder;
    private @FXML
    VBox searchMomentButton;
    private @FXML
    HBox containerMomentsTypes;
    private @FXML
    TemplateSpaceController templateSpaceController;
    GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();
    public Project project;
    private SchemaTreeRoot schemaTreeRoot;
    private List<MomentTypeController> currentMomentTypeControllers;
    public static ToolBoxControllers instance;

    public static ToolBoxControllers getToolBoxControllersInstance(Project project) {
        if (instance == null) {
            instance = new ToolBoxControllers();
            instance.project = project;
            instance.schemaTreeRoot = project.getSchemaTreeRoot();
            instance.currentMomentTypeControllers = new LinkedList<>();
        }

        if (!instance.project.equals(project)) {
            instance.project = project;
            instance.schemaTreeRoot = project.getSchemaTreeRoot();
            instance.currentMomentTypeControllers = new LinkedList<>();
        }

        for (MomentTypeController mtc : instance.project.getMomentTypeControllers()) {
            // lINKAGE
            mtc.getSchemaMomentType().setMomentTypeController(mtc);
            if (!instance.currentMomentTypeControllers.contains(mtc)) {
                instance.currentMomentTypeControllers.add(mtc);
            }
        }

        return instance;
    }

    public static ToolBoxControllers getToolBoxControllersInstance() {
        return instance;
    }

    public Node createToolBoxControllers(ToolBoxControllers controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(controller.getClass().getResource("/views/Toolbox/Toolbox.fxml"));
            fxmlLoader.setController(controller);
            fxmlLoader.setResources(Configuration.langBundle);

            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initSearchButton();
        updateGraphics();
        setupDragAndDrop();

    }

    private void updateGraphics() {
        for (MomentTypeController mtc : instance.currentMomentTypeControllers) {
            this.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(mtc));
        }
    }

    private void setupDragAndDrop() {
        /* On vérifie que l'on y glisse bien un moment */
        instance.containerMomentsTypes.setOnDragOver(dragEvent -> {
            if (DragStore.getDraggable().getDataFormat() == Moment.format) {
                if (canBeDragged(DragStore.getDraggable())) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    instance.containerMomentsTypes.setStyle(" -fx-background-color: #bdc3c7;");
                    dragEvent.consume();
                }
            } else {
                dragEvent.acceptTransferModes(TransferMode.NONE);
            }
        });

        /* Quand un élément est drag on l'ajoute au modèle + toolbox */
        instance.containerMomentsTypes.setOnDragDropped(dragEvent -> {
            if (DragStore.getDraggable().getDataFormat() == Moment.format) {
                Moment m = DragStore.getDraggable();
                if (canBeDragged(DragStore.getDraggable())) {
                    instance.addMomentTypeCommand(m);
                }
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });

        /* Quand un draggable sort de draggableMomentsType */
        instance.containerMomentsTypes.setOnDragExited(dragEvent -> {
            instance.containerMomentsTypes.setStyle(" -fx-background-color: none;");
            dragEvent.consume();
        });
    }

    // permet d'être utilisé dans l'arbre à gauche pour créer un type de moment
    public void addMomentTypeCommand(Moment m) {
        MomentTypeController momentTypeController = new MomentTypeController(m);
        SchemaMomentType smt = new SchemaMomentType(m, momentTypeController);
        momentTypeController.schemaMomentType=smt;
        new AddSchemaMomentTypeCommand(instance, smt).execute();
    }

    public void removeMomentTypeCommand(SchemaMomentType smt) {
        new RemoveSchemaMomentTypeCommand(instance, smt).execute();
    }

    //  permet d'être utilisé dans l'arbre à gauche pour créer un type de moment
    public boolean canBeDragged(Moment m) {
        for (MomentTypeController momentTypeController : instance.currentMomentTypeControllers) {
            if (momentTypeController.exists(m)) {
                return false;
            }
        }
        return true;
    }

    public void addAMomentType(SchemaMomentType schemaMomentType) {
        MomentTypeController momentTypeController = schemaMomentType.getMomentTypeController();
        instance.currentMomentTypeControllers.add(momentTypeController);
        instance.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(momentTypeController));
        instance.schemaTreeRoot.addChild(momentTypeController.getSchemaMomentType());
        instance.project.getMomentTypeControllers().add(momentTypeController);
    }

    public void removeAMomentType(SchemaMomentType schemaMomentType) {
        // Remove only from Toolbox. To remove from SchemaTree use removeTreeElement
        for (MomentTypeController momentTypeController : instance.project.getMomentTypeControllers()) {
            if (momentTypeController.getSchemaMomentType().getName().equals(schemaMomentType.getName())) {
                instance.schemaTreeRoot.removeChild(momentTypeController.getSchemaMomentType());
                instance.project.getMomentTypeControllers().remove(momentTypeController);

                if (instance.currentMomentTypeControllers.contains(momentTypeController)) { //si le momentType est affiché, on le supprime de la vue
                    instance.containerMomentsTypes.getChildren().remove(instance.currentMomentTypeControllers.indexOf(momentTypeController));
                    instance.currentMomentTypeControllers.remove(momentTypeController);
                }
                break;
            }
        }
    }

    public void hide(MomentTypeController mtc) {
        this.containerMomentsTypes.getChildren().remove(instance.currentMomentTypeControllers.indexOf(mtc));
        this.currentMomentTypeControllers.remove(mtc);
    }

    public void show(MomentTypeController mtc) {
        this.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(mtc));
        this.currentMomentTypeControllers.add(mtc);
    }

    public List<MomentTypeController> getCurrentMomentTypeControllers() {
        return currentMomentTypeControllers;
    }

    public void initSearchButton() {
        Canvas canvas = new Canvas(40, 40);
        this.searchMomentButton.setAlignment(Pos.CENTER);
        this.searchMomentButton.setPadding(new Insets(3));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.searchMomentButton.getChildren().add(canvas);
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, 20, 20);
        gc.drawImage(new Image("/images/book.png"), 0, 0, 40, 40);
        Tooltip tooltip = new Tooltip(Configuration.langBundle.getString("search_moment_toolTip"));
        tooltip.setShowDelay(new Duration(500));
        Tooltip.install(this.searchMomentButton, tooltip);

        this.searchMomentButton.setOnMouseClicked((e) ->
        {
            e.consume();
            globalVariables.isMomentSearchClicked.set(true);
        });
    }

}

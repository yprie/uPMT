package components.toolbox.controllers;

import application.configuration.Configuration;
import components.templateSpace.controllers.TemplateSpaceController;
import components.toolbox.history.commands.AddMomentTypeCommand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import models.Moment;
import models.Project;
import models.SchemaFolder;
import models.SchemaTreeRoot;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class ToolBoxControllers extends HBox implements Initializable {
    private @FXML HBox bodyToolBox;
    private @FXML Button addMomentTypeFolder;
    private @FXML HBox containerMomentsTypes;
    private @FXML TemplateSpaceController templateSpaceController;

    public Project project;
    private SchemaTreeRoot schemaTreeRoot;
    private List<MomentTypeController> currentMomentTypeControllers;
    private AddMomentTypeCommand addMomentTypeCommand;
    private SchemaFolder momentTypesSchemaTree;
    public static ToolBoxControllers instance;

    public static ToolBoxControllers getToolBoxControllersInstance(Project project) {
        if (instance == null) {
            instance = new ToolBoxControllers();
            instance.project = project;
            instance.schemaTreeRoot = project.getSchemaTreeRoot();
            instance.currentMomentTypeControllers = new LinkedList<MomentTypeController>();
            instance.momentTypesSchemaTree = instance.containMomentTypesSchemaTree();
        }

        if (!instance.project.equals(project)) {
            instance.project = project;
            instance.schemaTreeRoot = project.getSchemaTreeRoot();
            instance.currentMomentTypeControllers = new LinkedList<MomentTypeController>();
            instance.momentTypesSchemaTree = instance.containMomentTypesSchemaTree();
        }

        for (MomentTypeController mtc : instance.project.getMomentTypeControllers()) {
            // lINKAGE
            mtc.getMomentType().setMomentTypeController(mtc);
            mtc.getSchemaMomentType().setMomentTypeController(mtc);
            if (!instance.currentMomentTypeControllers.contains(mtc)) {
                instance.currentMomentTypeControllers.add(mtc);
                instance.momentTypesSchemaTree.addChild(mtc.getSchemaMomentType());
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
            }
            else {
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
        instance.addMomentTypeCommand = new AddMomentTypeCommand(instance, m);
        instance.addMomentTypeCommand.execute();
    }

    //  permet d'être utilisé dans l'arbre à gauche pour créer un type de moment
    public boolean canBeDragged(Moment m) {
        for (MomentTypeController momentTypeController : instance.currentMomentTypeControllers) {
            /* TODO créer une méthode qui permet de comparer les noms des catégories */
            if (momentTypeController.exists(m)) {
                return false;
            }
        }
        return true;
    }

    public void addAMomentType(Moment moment) {
        MomentTypeController momentTypeController = new MomentTypeController(moment);
        instance.currentMomentTypeControllers.add(momentTypeController);
        instance.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(momentTypeController));
        instance.momentTypesSchemaTree.addChild(momentTypeController.getSchemaMomentType());
        instance.project.getMomentTypeControllers().add(momentTypeController);
    }

    public void removeAMomentType(Moment moment) {
        for(MomentTypeController momentTypeController : instance.currentMomentTypeControllers) {
            if (momentTypeController.getMomentType().getName().equals(moment.getName())) {
                instance.momentTypesSchemaTree.removeChild(momentTypeController.getSchemaMomentType());
                instance.containerMomentsTypes.getChildren().remove(instance.currentMomentTypeControllers.indexOf(momentTypeController));
                instance.currentMomentTypeControllers.remove(momentTypeController);
                instance.project.getMomentTypeControllers().remove(momentTypeController);
            }
        }
    }

    public SchemaFolder containMomentTypesSchemaTree() {
        for (SchemaFolder sf : instance.schemaTreeRoot.foldersProperty()) {
            if (sf.getName().equals("Types de Moment")) {
                return sf;
            }
        }
        SchemaFolder momentTypesFolder = new SchemaFolder("Types de Moment");
        instance.schemaTreeRoot.addChild(momentTypesFolder);
        return momentTypesFolder;
    }
}

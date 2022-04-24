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

/* TODO
 * ??? : Bug, lorsque l'on drop un momentType dans la modélisation, son parent vaut null : modifier MoveMomentCommmand <= ????
 * split tollBoxControllers pour que ce soit plus propre
 * DONE 1 : TemplateSpaceController à mettre dans la tool box, il suffit de mettre le fxml dans celui de la tool box et changer le chemin de setting dans son .java
 * DONE 2 : Rendre le drag n drop possible dans la tool box, on vérifie qu'on y glisse bien un Moment
 * DONE 3 : Quand on drag un moment il apparait dans le modèle (mais sous forme de folder)
 * DONE 4 : Quand on drag un moment dans le tool box on peut le drag à nouveau dans la modélisation
 * DONE 5 : Copie profonde, création d'un type de moment dans la modélisation lors du drag + ajout couleur
 * DONE 6 : Pas de doublons + Pas de descripteme (Justification) + Pas de valeurs de propriétés
 * DONE 7 : Trouver une îcone pour la toolbox + historique
 * DONE 8 : Bug de copie profonde pour les catégories + Refactor des noms
 * DONE 9 : BUG on peut ajouter un truc de la toolbox vers la toolbox + curseur NONE si on ne peut pas drag
 * DONE 10 : Historique arbre gauche + toolbox lié et j'ai aussi fait en sorte que l'on puisse drag de l'arbre à gauche vers la modélisation
 * BUG : lorsqu'on change de langue ça recharge cette page et donc ça supprime tout (pcq on recharge la hbox) ...
 * DONE 11 :  renommage d'un type de moments via le modèle, mais c'est dégeu, faudra le refaire ! (la liaison dans momentType est moche, faut la faire dans le controller, y'a bcp de l'architecture à refaire)
 * DONE 12 : Refactor, correction d'un bug, passage en mvc PUR
 * DONE 12 à faire : continuer le refactor, notamment les changements dans le SchemaTreeRoot (la fonction en bas)
 * DONE 13 : vérifier un peu le code et faire en sorte que l'on puisse créer un mtc quand on glisse le moment vers l'arbre à gauche (HF) :)
 * DONE 14 : Rendre le changement de langue persistant (TOUT PASSER EN SIGNLETON + faire un methode update graphics
 */

public class ToolBoxControllers extends HBox implements Initializable {
    private @FXML HBox bodyToolBox;
    private @FXML Button addMomentTypeFolder;
    private @FXML HBox containerMomentsTypes;
    private @FXML TemplateSpaceController templateSpaceController;

    private Project project;
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
        return instance;
    }

    public static ToolBoxControllers getToolBoxControllersInstance() {
        return instance;
    }

    public Node createToolBoxControllers(ToolBoxControllers controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Toolbox/Toolbox.fxml"));
            fxmlLoader.setController(controller);
            fxmlLoader.setResources(Configuration.langBundle);

            return fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    private void updateGraphics() {
        for (MomentTypeController mtc : instance.currentMomentTypeControllers) {
            this.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(mtc));
        }
    }

    private void setupDragAndDrop() {
        /* On vérifie que l'on y glisse bien un moment */
        instance.containerMomentsTypes.setOnDragOver(dragEvent -> {
            if (canBeDragged(DragStore.getDraggable())) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
                instance.containerMomentsTypes.setStyle(" -fx-background-color: #bdc3c7;");
                dragEvent.consume();
            } else {
                dragEvent.acceptTransferModes(TransferMode.NONE);
            }
        });

        /* Quand un élément est drag on l'ajoute au modèle + toolbox */
        instance.containerMomentsTypes.setOnDragDropped(dragEvent -> {
            Moment m = DragStore.getDraggable();
            if (canBeDragged(m)) {
                instance.addMomentTypeCommand(m);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateGraphics();
        setupDragAndDrop();
    }

    // permet d'être utilisé dans l'arbre à gauche pour créer un type de moment
    public void addMomentTypeCommand(Moment m) {
        instance.addMomentTypeCommand = new AddMomentTypeCommand(instance, m);
        instance.addMomentTypeCommand.execute();
    }

    //  permet d'être utilisé dans l'arbre à gauche pour créer un type de moment
    public boolean canBeDragged(Moment m) {
        if (DragStore.getDraggable().getDataFormat() == Moment.format) {
            for (MomentTypeController momentTypeController : instance.currentMomentTypeControllers) {
                /* TODO créer une méthode qui permet de comparer les noms des catégories */
                if (momentTypeController.exists(m)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addAMomentType(Moment moment) {
        MomentTypeController momentTypeController = new MomentTypeController(moment);
        instance.currentMomentTypeControllers.add(momentTypeController);
        instance.containerMomentsTypes.getChildren().add(MomentTypeController.createMomentTypeController(momentTypeController));
        instance.momentTypesSchemaTree.addChild(momentTypeController.getSchemaMomentType());
    }

    public void removeAMomentType(Moment moment) {
        for(MomentTypeController momentTypeController : instance.currentMomentTypeControllers) {
            if (momentTypeController.getMomentType().getName().equals(moment.getName())) {
                instance.momentTypesSchemaTree.removeChild(momentTypeController.getSchemaMomentType());
                instance.containerMomentsTypes.getChildren().remove(instance.currentMomentTypeControllers.indexOf(momentTypeController));
                instance.currentMomentTypeControllers.remove(momentTypeController);
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

package components.toolbox.initalizable;

import application.configuration.Configuration;
import components.templateSpace.controllers.TemplateSpaceController;
import components.toolbox.initalizable.commands.AddMomentTypeCommand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import models.*;
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
 */

public class ToolBoxControllers extends HBox implements Initializable {
    private @FXML HBox bodyToolBox;
    private @FXML Button addMomentTypeFolder;
    private @FXML HBox containerMomentsTypes;
    private @FXML TemplateSpaceController templateSpaceController;

    private Project project;
    private SchemaTreeRoot schemaTreeRoot;
    private List<Moment> currentMomentsTypesList;
    /* TODO faire un cmdFactory pour avoir toutes les commandes en 1 variable */
    private AddMomentTypeCommand amtc;
    private SchemaFolder momentTypesFolder;

    public Node createToolBoxControllers(ToolBoxControllers controller, Project project) {
        try {
            this.project = project;
            this.schemaTreeRoot = project.getSchemaTreeRoot();
            this.currentMomentsTypesList = new LinkedList<>();
            this.momentTypesFolder = new SchemaFolder("Types de Moment");
            this.schemaTreeRoot.addChild(this.momentTypesFolder);


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/Toolbox/Toolbox.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    private boolean canBeDragged(Moment m) {
        if (DragStore.getDraggable().getDataFormat() == Moment.format) {
            for (Moment moment : this.currentMomentsTypesList) {
                /* TODO créer une méthode qui permet de comparer les noms des catégories */
                if (moment.getName().equals(m.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setupDragAndDrop() {
        /* On vérifie que l'on y glisse bien un moment */
        containerMomentsTypes.setOnDragOver(dragEvent -> {
            if (canBeDragged(DragStore.getDraggable())) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
                containerMomentsTypes.setStyle(" -fx-background-color: #bdc3c7;");
                dragEvent.consume();
            } else {
                dragEvent.acceptTransferModes(TransferMode.NONE);
            }
        });

        /* Quand un élément est drag on l'ajoute au modèle + toolbox */
        containerMomentsTypes.setOnDragDropped(dragEvent -> {
            Moment m = DragStore.getDraggable();
            if (canBeDragged(m)) {
                /* TODO factory command */
                this.amtc = new AddMomentTypeCommand(this, m);
                amtc.execute();
            }
            dragEvent.setDropCompleted(true);
            dragEvent.consume();
        });

        /* Quand un draggable sort de draggableMomentsType */
        containerMomentsTypes.setOnDragExited(dragEvent -> {
            containerMomentsTypes.setStyle(" -fx-background-color: none;");
            dragEvent.consume();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupDragAndDrop();
    }


    public HBox getContainerMomentsTypes() {
        return containerMomentsTypes;
    }

    public List<Moment> getCurrentMomentsTypesList() {
        return currentMomentsTypesList;
    }

    public SchemaFolder getMomentTypesFolder() {
        return momentTypesFolder;
    }
}

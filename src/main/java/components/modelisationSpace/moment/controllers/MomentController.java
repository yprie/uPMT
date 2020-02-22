package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import components.modelisationSpace.moment.model.Moment;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import utils.modelControllers.HBox.HBoxModel;
import utils.modelControllers.HBox.HBoxModelController;
import utils.modelControllers.HBox.HBoxModelUpdate;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class MomentController extends HBoxModelController<Moment> implements Initializable {

    private Moment moment;
    private MomentCommandFactory cmdFactory;
    private MomentCommandFactory childCmdFactory;
    private ScrollPaneCommandFactory paneCmdFactory;

    @FXML private BorderPane momentContainer;
    @FXML private BorderPane momentBody;
    @FXML private Label momentName;
    @FXML private Button btn;
    @FXML private MenuButton menuButton;

    //Importants elements of a moment
    private JustificationController justificationController;
    private HBoxModel<Moment, MomentController> momentsHBox;

    @FXML private GridPane grid;
    MomentSeparatorController separatorLeft, separatorRight, separatorBottom;

    public MomentController(Moment m, MomentCommandFactory cmdFactory, ScrollPaneCommandFactory paneCmdFactory) {
        this.moment = m;
        this.cmdFactory = cmdFactory;
        this.childCmdFactory = new MomentCommandFactory(moment);
        this.paneCmdFactory = paneCmdFactory;

        separatorLeft = new MomentSeparatorController(true);
        separatorRight = new MomentSeparatorController(true);
        separatorBottom = new MomentSeparatorController(false);

        this.justificationController = new JustificationController(m.getJustification());
    }

    public static Node createMoment(MomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/moment/Moment.fxml"));
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
        grid.add(separatorBottom.getNode(), 1, 1);
        momentName.setText(moment.getName());

        //Setup de la zone de DND des descriptemes
        momentBody.setCenter(JustificationController.createJustificationArea(justificationController));
        //Setup de la HBox pour les enfants
        momentsHBox = new HBoxModel<Moment, MomentController>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment);
        momentsHBox.setOnListUpdate(change -> {
            if(change.getList().size() == 0)
                separatorBottom.setActive(true);
            else
                separatorBottom.setActive(false);
        });
        momentContainer.setCenter(momentsHBox);


        //Listeners SETUP
        //bottom separator works only when there is no child yet !
        separatorBottom.setOnDragDone(descripteme -> {
                childCmdFactory.addSiblingCommand(new Moment("Moment", descripteme)).execute();
        });
        separatorBottom.setActive(moment.momentsProperty().size() == 0);

        //Menu Button
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.deleteCommand(moment).execute();
        });
        menuButton.getItems().add(deleteButton);
    }

    @Override
    public Moment getModel() {
        return moment;
    }

    @Override
    public void onMount() {
        paneCmdFactory.scrollToNode(momentContainer).execute();
    }

    @Override
    public void onUpdate(HBoxModelUpdate update) {
        updateBorders(update.newIndex, update.totalCount);
    }

    @Override
    public void onUnmount() {
        momentsHBox.onUnmount();
    }

    private void updateBorders(int index, int siblingsCount) {
        if(index == 0) {
            //Hide an show the separators
            if(grid.getChildren().indexOf(separatorLeft.getNode()) == -1)
                grid.add(separatorLeft.getNode(), 0, 0);
            if(grid.getChildren().indexOf(separatorRight.getNode()) == -1)
                grid.add(separatorRight.getNode(), 2, 0);

            //set operation on descripteme DND over borders
            separatorLeft.setOnDragDone(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), 0).execute(); });
            separatorRight.setOnDragDone(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), index+1).execute(); });

            //Make moment aligned, no need to understand that !
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), ins.getRight()));
        }
        else {
            //Hide an show the separators
            if(grid.getChildren().indexOf(separatorLeft.getNode()) != -1)
                grid.getChildren().remove(separatorLeft.getNode());
            if(grid.getChildren().indexOf(separatorRight.getNode()) == -1)
                grid.add(separatorRight.getNode(), 2, 0);

            //Do nothing with the left separator
            separatorLeft.setOnDragDone(descripteme -> {});
            if(index == siblingsCount - 1) {
                separatorRight.setOnDragDone(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme)).execute(); });
            }
            else {
                separatorRight.setOnDragDone(descripteme -> { cmdFactory.addSiblingCommand(new Moment("Moment", descripteme), index+1).execute(); });
            }

            //Make moment aligned, no need to understand that !
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), 0));
        }
    }

}

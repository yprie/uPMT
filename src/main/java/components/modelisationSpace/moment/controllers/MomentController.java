package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import components.interviewPanel.Models.Descripteme;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import components.modelisationSpace.moment.model.Moment;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML private BorderPane momentContainer;
    @FXML private Label momentName;
    @FXML private Button btn;

    private HBoxModel<Moment, MomentController> momentsHBox;

    @FXML private GridPane grid;
    MomentSeparatorController separatorLeft, separatorRight, separatorBottom;

    private ListChangeListener<Moment> childChangeListener = change -> {
        while(change.next()) {
            for (Moment remitem : change.getRemoved()) {
                momentsHBox.remove(remitem);
            }
            for (Moment additem : change.getAddedSubList()) {
                momentsHBox.add(change.getTo()-1, additem);
            }
        }
    };

    public MomentController(Moment m, MomentCommandFactory cmdFactory) {
        this.moment = m;
        this.cmdFactory = cmdFactory;
        this.childCmdFactory = new MomentCommandFactory(moment);

        separatorLeft = new MomentSeparatorController(true);
        separatorRight = new MomentSeparatorController(true);
        separatorBottom = new MomentSeparatorController(false);
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

        //Setup de la HBox pour les enfants
        momentsHBox = new HBoxModel<Moment, MomentController>(
                (m -> new MomentController(m, childCmdFactory)),
                MomentController::createMoment);

        for(Moment m : moment.momentsProperty()) {
            momentsHBox.add(m);
        }
        momentContainer.setCenter(momentsHBox);
        moment.momentsProperty().addListener(childChangeListener);

        //bottom separator works only when there is no child yet !
        separatorBottom.setOnDragDone(descripteme -> {
            if(moment.momentsProperty().size() < 1)
                childCmdFactory.addSiblingCommand(new Moment("Moment", descripteme)).execute();
        });
    }

    @Override
    public Moment getModel() {
        return moment;
    }

    @Override
    public void onUpdate(HBoxModelUpdate update) {
        updateBorders(update.newIndex, update.totalCount);
    }

    @Override
    public void onUnmount() {
        moment.momentsProperty().removeListener(childChangeListener);
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
        }
    }
}

package components.interviewSelector;

import components.interviewSelector.appCommands.InterviewSelectorCommandFactory;
import components.interviewSelector.controllers.InterviewSelectorCellController;
import components.modelisationSpace.controllers.ModelisationSpaceController;
import models.Interview;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

public class InterviewSelectorCell extends ListCell<Interview> {

    private InterviewSelectorCellController controller;
    private InterviewSelectorCommandFactory commandFactory;
    private ModelisationSpaceController modelisationSpaceController;

    public InterviewSelectorCell(InterviewSelectorCommandFactory commandFactory, ModelisationSpaceController modelisationSpaceController) {
        this.commandFactory = commandFactory;
        this.modelisationSpaceController = modelisationSpaceController;
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> { if(controller != null)controller.setOnHover(true); });
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {if(controller != null)controller.setOnHover(false); });
    }

    @Override
    public void updateItem(Interview item, boolean empty) {
        super.updateItem(item, empty);

        if(empty)
            removeGraphics();
        else {
            //Cell view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/InterviewSelector/InterviewSelectorCell.fxml"));
            //Cell Controller
            InterviewSelectorCellController newController = new InterviewSelectorCellController(item, commandFactory, modelisationSpaceController);
            loader.setController(newController);
            controller = newController;

            //Mouse click event
            setOnMouseClicked(event -> commandFactory.selectCurrentInterview(item, true).execute());

            try {
                this.setGraphic(loader.load());
            } catch (Exception ex) {
                System.out.println("Error on ModelTreeCell graphics update !");
                ex.printStackTrace();
            }
        }
    }

    private void removeGraphics() {
        this.setGraphic(null);
    }
}

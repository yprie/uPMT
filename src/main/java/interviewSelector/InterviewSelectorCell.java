package interviewSelector;

import interviewSelector.commands.InterviewSelectorCommandFactory;
import interviewSelector.commands.SelectCurrentInterviewCommand;
import interviewSelector.controllers.InterviewSelectorCellControler;
import interviewSelector.Models.Interview;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

public class InterviewSelectorCell extends ListCell<Interview> {

    private InterviewSelectorCellControler controller;
    private InterviewSelectorCommandFactory commandFactory;

    public InterviewSelectorCell(InterviewSelectorCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
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
            controller = new InterviewSelectorCellControler(item, commandFactory);
            InterviewSelectorCellControler newController = controller;
            loader.setController(newController);
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

package interviewSelector;

import interviewSelector.Controllers.InterviewListCellControler;
import interviewSelector.Models.Interview;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class InterviewListCell extends ListCell<Interview> {

    @Override
    public void updateItem(Interview item, boolean empty) {
        super.updateItem(item, empty);

        if(empty)
            removeGraphics();
        else {
            //Cell view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/InterviewTree/InterviewTree.fxml"));
            //Cell Controller
            InterviewListCellControler newController = new InterviewListCellControler(item);
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

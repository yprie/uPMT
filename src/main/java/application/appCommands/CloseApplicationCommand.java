package application.appCommands;

import application.UPMTApp;
import application.configuration.Configuration;
import application.history.HistoryManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import java.util.Optional;
import java.util.UUID;

public class CloseApplicationCommand extends ApplicationCommand<Void> {

    public CloseApplicationCommand(UPMTApp application, WindowEvent event) {

        super(application);
        this.event = event;
    }

    WindowEvent event;

    @Override
    public Void execute() {

        System.out.println("Test checking project unsaved");
        //TODO check for unsaved work
        boolean workUnsaved = false;
        String currentTitle = upmtApp.getPrimaryStage().getTitle();
        UUID currentCommandId = HistoryManager.getCurrentCommandId();
        UUID lastSavedCommandId = upmtApp.getLastSavedCommandId();
        if(currentCommandId != null ){
            if(lastSavedCommandId == null){
                workUnsaved = true;
            }else {
                if (HistoryManager.getCurrentCommandId().equals(lastSavedCommandId)) {
                    System.out.println("Projet sauvegardé");
                } else {
                    workUnsaved = true;
                }
            }
        }
        if(workUnsaved) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("");
            alert.setTitle(Configuration.langBundle.getString("alert_unsaved_project_title"));
            alert.setContentText(Configuration.langBundle.getString("alert_unsaved_project"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            } else {
                System.out.println("Cancel click");
                event.consume();
            }
        }
        return null;
    }
}

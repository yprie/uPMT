package application.appCommands;

import application.UPMTApp;
import application.configuration.Configuration;
import application.history.HistoryManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.Optional;
import java.util.UUID;

public class CloseApplicationCommand extends ApplicationCommand<Void> {

    public CloseApplicationCommand(ApplicationCommandFactory appCommandFactory, UPMTApp application, WindowEvent event) {

        super(application);
        this.event = event;
        this.appCommandFactory = appCommandFactory;
    }

    WindowEvent event;
    ApplicationCommandFactory appCommandFactory;

    @Override
    public Void execute() {

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
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

            ButtonType buttonTypeOne = new ButtonType(Configuration.langBundle.getString("alert_unsaved_project_buttonTypeOne"));
            ButtonType buttonTypeTwo = new ButtonType(Configuration.langBundle.getString("alert_unsaved_project_buttonTypeTwo"));
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                // ... user chose "Save And Quit"
                appCommandFactory.saveProject().execute();
                System.exit(0);
            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Quit without saving"
                System.exit(0);
            } else {
                // ... user chose CANCEL or closed the dialog
                event.consume();
            }
        }
        return null;
    }
}

package application.appCommands;

import application.UPMTApp;
import application.configuration.AppSettings;
import application.configuration.Configuration;
import javafx.scene.control.Alert;

import java.io.IOException;

public class SetAutoScrollWhenRevealCommand extends ApplicationCommand<Void> {
    boolean newAutoScrollWhenReveal;

    public SetAutoScrollWhenRevealCommand(UPMTApp application, boolean autoScrollWhenReveal) {
        super(application);
        this.newAutoScrollWhenReveal = autoScrollWhenReveal;
    }

    @Override
    public Void execute() {
        AppSettings.autoScrollWhenReveal.set(this.newAutoScrollWhenReveal);
        try {
            Configuration.savePropertiesFile();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Configuration.langBundle.getString("error"));
            alert.setHeaderText(Configuration.langBundle.getString("application_settings_failed"));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            alert.showAndWait();
        }
        return null;
    }
}

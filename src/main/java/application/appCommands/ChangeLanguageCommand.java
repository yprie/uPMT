package application.appCommands;

import application.configuration.Configuration;
import application.UPMTApp;
import javafx.scene.control.Alert;

import java.util.Locale;

public class ChangeLanguageCommand extends ApplicationCommand<Void> {

    private Locale locale;

    public ChangeLanguageCommand(UPMTApp application, Locale locale) {
        super(application);
        this.locale = locale;
    }

    @Override
    public Void execute() {
        try {
            Configuration.setLocale(locale);
            upmtApp.restartApp();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Configuration.langBundle.getString("error"));
            alert.setHeaderText(Configuration.langBundle.getString("application_language_change_failed"));
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            alert.showAndWait();
        }
        return null;
    }
}

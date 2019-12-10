package application.Commands;

import application.Configuration.Configuration;
import application.UPMTApp;
import javafx.scene.control.Alert;

import java.io.IOException;
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
            alert.setTitle("Error");
            alert.setHeaderText("Unable to change the program language !");
            alert.showAndWait();
        }
        return null;
    }
}

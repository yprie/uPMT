package application.project.controllers;

import application.configuration.Configuration;
import javafx.scene.control.Alert;

public class ProjectDialogBox {

    public static void projectCreationFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Configuration.langBundle.getString("error"));
        alert.setHeaderText(Configuration.langBundle.getString("project_creating_error_occured"));
        alert.getDialogPane().getStylesheets().add(alert.getClass().getResource("/css/application.css").toExternalForm());
        alert.showAndWait();
    }

    public static void projectLoadingFailed () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Configuration.langBundle.getString("error"));
        alert.setHeaderText(Configuration.langBundle.getString("project_loading_error_occured"));
        alert.setContentText(Configuration.langBundle.getString("project_loading_error_reason"));
        alert.getDialogPane().getStylesheets().add(alert.getClass().getResource("/css/application.css").toExternalForm());
        alert.showAndWait();
    }

    public static void projectSavingFailed () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Configuration.langBundle.getString("error"));
        alert.setHeaderText(Configuration.langBundle.getString("project_saving_error_occured"));
        alert.getDialogPane().getStylesheets().add(alert.getClass().getResource("/css/application.css").toExternalForm());
        alert.showAndWait();
    }
}

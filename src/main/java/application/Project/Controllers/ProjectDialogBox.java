package application.Project.Controllers;

import javafx.scene.control.Alert;

public class ProjectDialogBox {

    public static void projectCreationFailed() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to create a new project !");
        alert.setContentText("Please contact us to know more about this issue.");
        alert.showAndWait();
    }

    public static void projectLoadingFailed () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Loading of the project failed !");
        alert.setContentText("Please make sure this is a valid .upmt file.");
        alert.showAndWait();
    }

    public static void projectSavingFailed () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("The project saving process failed !");
        alert.showAndWait();
    }
}

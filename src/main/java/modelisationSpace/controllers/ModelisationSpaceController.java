<<<<<<< Updated upstream
package Components.ModelisationSpace.Controllers;
=======
package modelisationSpace.controllers;
>>>>>>> Stashed changes

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
import java.io.IOException;

public class ModelisationSpaceController extends ScrollPane {

    public ModelisationSpaceController() {
<<<<<<< Updated upstream
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ModelisationSpace/ModelisationSpace.fxml"));
=======
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modelisationSpace/ModelisationSpace.fxml"));
>>>>>>> Stashed changes
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}


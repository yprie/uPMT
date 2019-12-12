package application.Project.Controllers;

import application.Project.Models.Project;
import Persistency.ProjectSaver;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SaveAsProjectController {

    private State state;
    public enum State {CLOSED, SUCCESS}
    private String savePath;

    public static SaveAsProjectController createSaveAsProjectController(Stage stage, Project project) {
        return new SaveAsProjectController(stage, project);
    }

    public SaveAsProjectController(Stage stage, Project project) {
        this.state = State.CLOSED;

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a Project");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("uPMT", "*.upmt"));
        File file = fileChooser.showSaveDialog(stage);
        if(file != null){
            try {
                this.savePath = file.getPath();
                this.state = State.SUCCESS;
            } catch (Exception e) {
                ProjectDialogBox.projectSavingFailed();
            }
        }
    }

    public State getState() { return state; }
    public String getSavePath() { return savePath; }
}

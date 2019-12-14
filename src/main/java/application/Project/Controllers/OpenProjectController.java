package application.Project.Controllers;

import application.Configuration.Configuration;
import application.Project.Models.Project;
import Persistency.ProjectLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class OpenProjectController {

    private State state;
    public enum State {CLOSED, SUCCESS}
    private Project resultProject;
    private String projectPath;

    public static OpenProjectController createOpenProjectController(Stage stage) {
        OpenProjectController controller = new OpenProjectController(stage);
        return controller;
    }

    public OpenProjectController(Stage stage) {
        this.state = State.CLOSED;

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Configuration.langBundle.getString("open_project_as"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("uPMT", "*.upmt"));
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            try {
                projectPath = file.getPath();
                resultProject = ProjectLoader.load(projectPath);
                this.state = State.SUCCESS;
            } catch (Exception e) {
                ProjectDialogBox.projectLoadingFailed();
            }
        }
    }

    public State getState() { return state; }
    public Project getResultProject() { return resultProject; }
    public String getProjectPath() { return projectPath; }

}

package Project.Controllers;

import Project.Models.Project;
import SchemaTree.Cell.Models.SchemaTreeRoot;
import application.Configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectController implements Initializable  {

    public enum State {CLOSED, SUCCESS}
    private State state;

    private @FXML Button valider;
    private @FXML Button cancel;
    private @FXML ChoiceBox<SchemaTreeRoot> choixSchema;
    private @FXML TextField nomProjet;

    private Stage stage;
    private Project resultProject;

    public static NewProjectController createNewProject() {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Configuration.langBundle.getString("new_project"));
        NewProjectController controller = new NewProjectController(stage);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("../Views/ProjectSelectionView.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            BorderPane layout = (BorderPane) loader.load();
            Scene main = new Scene(layout,404,250);
            stage.setScene(main);
            stage.showAndWait();
            return controller;
        } catch (IOException e) {
            // TODO Exit Program
            e.printStackTrace();
        }
        return null;
    }

    public NewProjectController(Stage stage) {
        this.stage = stage;
        this.state = State.CLOSED;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SchemaTreeRoot empty = new SchemaTreeRoot("Empty Schema");
        choixSchema.getItems().add(empty);
        choixSchema.setValue(empty);
    }

    public void createProject(){
        SchemaTreeRoot selectedRoot = choixSchema.getSelectionModel().getSelectedItem();
        selectedRoot.setName(nomProjet.getText());
        resultProject = new Project(nomProjet.getText(), selectedRoot);
        state = State.SUCCESS;
        stage.close();
    }

    public void closeWindow() {
        state = State.CLOSED;
        stage.close();
    }

    public State getState() { return state; }
    public Project getCreatedProject() { return resultProject; }
}
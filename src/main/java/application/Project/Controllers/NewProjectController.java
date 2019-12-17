package application.Project.Controllers;

import application.Project.Models.Project;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;
import application.Configuration.Configuration;
import utils.DialogState;

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
    private DialogState state;

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
            loader.setLocation(controller.getClass().getResource("/views/Project/NewProjectView.fxml"));
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
        this.state = DialogState.CLOSED;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SchemaTreeRoot empty = new SchemaTreeRoot(Configuration.langBundle.getString("empty_schema_tree_view"));
        choixSchema.getItems().add(empty);
        choixSchema.setValue(empty);

        valider.setDisable(true);
        valider.disableProperty().bind(nomProjet.lengthProperty().isEqualTo(0));
    }

    public void createProject(){
        SchemaTreeRoot selectedRoot = choixSchema.getSelectionModel().getSelectedItem();
        selectedRoot.setName(nomProjet.getText());
        resultProject = new Project(nomProjet.getText(), selectedRoot);
        state = DialogState.SUCCESS;
        stage.close();
    }

    public void closeWindow() {
        state = DialogState.CLOSED;
        stage.close();
    }

    public DialogState getState() { return state; }
    public Project getCreatedProject() { return resultProject; }
}
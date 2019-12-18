package application.project.controllers;

import application.project.models.Project;
import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaProperty;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import application.configuration.Configuration;
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
        SchemaTreeRoot example = createExampleSchemaTreeRoot();
        choixSchema.getItems().add(empty);
        choixSchema.getItems().add(example);
        choixSchema.setValue(example);

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


    private SchemaTreeRoot createExampleSchemaTreeRoot() {

        SchemaCategory bodilyFeeling = new SchemaCategory("Bodily Feeling");
        SchemaProperty temperature = new SchemaProperty("temperature");
        SchemaProperty shape = new SchemaProperty("shape");
        SchemaProperty location = new SchemaProperty("location");
        SchemaProperty density = new SchemaProperty("density");
        bodilyFeeling.addChild(temperature);
        bodilyFeeling.addChild(shape);
        bodilyFeeling.addChild(location);
        bodilyFeeling.addChild(density);

        SchemaCategory emotion = new SchemaCategory("Emotion");
        SchemaProperty type = new SchemaProperty("type");
        emotion.addChild(type);

        SchemaCategory auditorySensation = new SchemaCategory("Auditory sensation");
        SchemaProperty source = new SchemaProperty("source");
        SchemaProperty audit_location = new SchemaProperty("location");
        auditorySensation.addChild(source);
        auditorySensation.addChild(audit_location);

        SchemaFolder general = new SchemaFolder("General");
        general.addChild(bodilyFeeling);
        general.addChild(emotion);

        SchemaFolder sensation = new SchemaFolder("Sensation");
        sensation.addChild(auditorySensation);

        general.addChild(sensation);

        SchemaTreeRoot root = new SchemaTreeRoot("Example");
        root.addChild(general);
        return root;
    }
}
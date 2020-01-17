package application;

import application.history.HistoryManager;
import application.project.models.Project;
import application.appCommands.ApplicationCommandFactory;
import application.configuration.Configuration;

import components.rootLayout.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;
import persistency.newSaveSystem.*;
import persistency.newSaveSystem.serialization.json.JSONReadPool;
import persistency.newSaveSystem.serialization.json.JSONSerializer;
import persistency.newSaveSystem.serialization.json.JSONWritePool;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;


public class UPMTApp {

    private Stage primaryStage;
    private RootLayoutController rootLayoutController;
    private ApplicationCommandFactory appCommandFactory;
    private Project currentProject;
    private String currentProjectPath;
    private UUID lastSavedCommandId;

    public UPMTApp(Stage primaryStage) throws IOException {

        //Testing section !
        //Saving part
        /*int pr = 5;
        int r = 4;
        int i = 7;
        int it = 12;

        int f1 = 10;
        int c1 = 11;
        int p1 = 15;

        JSONObject obj = new JSONObject();
        JSONWritePool pool = new JSONWritePool();
        JSONSerializer serializer = new JSONSerializer(obj, pool);
        SProject p = new SProject(pr);

        p.name = "Mon super projet !";
        p.schemaTreeRoot = new SSchemaTreeRoot(r);

        p.schemaTreeRoot.name = "Noeud racine du projet";
        p.schemaTreeRoot.folders = new ArrayList<>();

        SSchemaFolder f1__ = new SSchemaFolder(f1);
        f1__.expanded = true;
        f1__.name = "awesome folder name";
        f1__.categories = new ArrayList<>();
        f1__.folders = new ArrayList<>();

        SSchemaCategory cat1 = new SSchemaCategory(c1);
        cat1.expanded = false;
        cat1.name = "Première catégorie !";
        cat1.properties = new ArrayList<>();

        SSchemaProperty prop1 = new SSchemaProperty(p1);
        prop1.expanded = false;
        prop1.name = "Super propriété !!!!";

        //cat1.properties.add(prop1);
        f1__.categories.add(cat1);

        p.schemaTreeRoot.folders.add(f1__);
        p.schemaTreeRoot.folders.add(f1__);

        p.interviews = new ArrayList<>();

        SInterview i1 = new SInterview(i);
        i1.comment = "Patient très agité";
        i1.participantName = "Jean-Jacques";
        i1.date = LocalDate.now();

        SInterviewText iText = new SInterviewText(it);
        iText.text = "Ceci est le texte incroyable de l'interview !";
        i1.interviewText = iText;

        p.interviews.add(i1);
        p.interviews.add(i1);
        p.interviews.add(i1);

        p.save(serializer);
        String jsonRepresentation = obj.toString(4);


        System.out.println(jsonRepresentation);

        //Reading part
        JSONTokener tokener = new JSONTokener(jsonRepresentation);
        JSONReadPool pool2 = new JSONReadPool();
        JSONSerializer serializer2 = new JSONSerializer(new JSONObject(tokener), pool2);

        SProject project2 = new SProject(serializer2);*/


        //END of testing section !

        this.primaryStage = primaryStage;
        this.appCommandFactory = new ApplicationCommandFactory(this);
        this.rootLayoutController = new RootLayoutController(appCommandFactory);

        Configuration.loadAppConfiguration();
        HistoryManager.init(appCommandFactory);

        Scene mainScene = new Scene(RootLayoutController.createRootLayout(rootLayoutController));
        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(event -> { appCommandFactory.closeApplication().execute(); });
        primaryStage.show();

        //Load the last used project or ask for a new one.
        if(Configuration.getProjectsPath().length > 0)
            appCommandFactory.openRecentProject(Configuration.getProjectsPath()[0]).execute();
        else
            appCommandFactory.openProjectManagerCommand().execute();



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/MainView/MainView.fxml"));
        loader.setResources(Configuration.langBundle);
        loader.setClassLoader(getClass().getClassLoader());
        loader.load();
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setCurrentProject(Project project, String path) {
        currentProject = project;
        currentProjectPath = path;
        rootLayoutController.setProject(project);
    }
    public Project getCurrentProject() {
        return currentProject;
    }
    public String getCurrentProjectPath() { return currentProjectPath; }

    public void setLastSavedCommandId(UUID lastCommandId) { this.lastSavedCommandId = lastCommandId; }
    public UUID getLastSavedCommandId() { return lastSavedCommandId; }

    public void restartApp() {
        primaryStage.getScene().setRoot(RootLayoutController.createRootLayout(rootLayoutController));
        if(getCurrentProject() != null)
            setCurrentProject(getCurrentProject(), currentProjectPath);
    }
    
}
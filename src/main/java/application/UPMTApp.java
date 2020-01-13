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
import persistency.newSaveSystem.SInterview;
import persistency.newSaveSystem.SProject;
import persistency.newSaveSystem.SSchemaTreeRoot;
import persistency.newSaveSystem.serialization.JSONSerializer;
import persistency.newSaveSystem.serialization.JSONSerializerPool;

import java.io.IOException;
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
        int pr = 5;
        int r = 4;
        int i = 10;

        JSONObject obj = new JSONObject();
        JSONSerializerPool pool = new JSONSerializerPool();
        JSONSerializer serializer = new JSONSerializer(obj, pool);
        SProject p = new SProject(pr);

        p.name = "Mon super projet !";
        p.schemaTreeRoot = new SSchemaTreeRoot(r);
        p.interviews = new ArrayList<>();
        p.interviews.add(new SInterview(i));
        p.interviews.add(new SInterview(i));

        p.save(serializer);
        System.out.println(obj.toString(4));

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
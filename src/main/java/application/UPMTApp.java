package application;

import application.appCommands.ApplicationCommandFactory;
import application.configuration.Configuration;
import application.history.HistoryManager;
import com.sun.javafx.css.StyleManager;
import components.rootLayout.Controllers.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import models.Project;
import utils.GlobalVariables;
import java.io.IOException;
import java.util.UUID;


public class UPMTApp {

    private Stage primaryStage;
    private RootLayoutController rootLayoutController;
    private ApplicationCommandFactory appCommandFactory;
    private static Project currentProject;
    private String currentProjectPath;
    private UUID lastSavedCommandId;

    private long autoSaveIntervalMillis;


    public UPMTApp(Stage primaryStage) throws IOException {


        this.primaryStage = primaryStage;
        this.appCommandFactory = new ApplicationCommandFactory(this);
        this.rootLayoutController = new RootLayoutController(appCommandFactory);
        this.autoSaveIntervalMillis = 5000;


        Configuration.loadAppConfiguration();
        HistoryManager.init(appCommandFactory);

        Scene mainScene = new Scene(RootLayoutController.createRootLayout(rootLayoutController));

        //set default stylesheet
        Application.setUserAgentStylesheet(null);
        StyleManager.getInstance().addUserAgentStylesheet(this.getClass().getResource("/css/application.css").toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(event -> appCommandFactory.closeApplication(event).execute());
        primaryStage.show();

        //Load the last used project or ask for a new one.
        if(Configuration.getProjectsPath().length > 0){
            appCommandFactory.openRecentProject(Configuration.getProjectsPath()[0]).execute();
        }
        else {
            Configuration.SetUpExampleProject(getClass());
            appCommandFactory.openProjectManagerCommand().execute();
        }

        startAutoSave();

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
        GlobalVariables.setCurrentProjectPath(currentProjectPath);
        rootLayoutController.setProject(project);
    }
    public static Project getCurrentProject() {
        return currentProject;
    }
    public String getCurrentProjectPath() { return currentProjectPath; }
    public void setCurrentProjectPath(String currentProjectPath) { this.currentProjectPath = currentProjectPath; }

    public void setLastSavedCommandId(UUID lastCommandId) { this.lastSavedCommandId = lastCommandId; }
    public UUID getLastSavedCommandId() { return lastSavedCommandId; }

    public void restartApp() {
        primaryStage.getScene().setRoot(RootLayoutController.createRootLayout(rootLayoutController));
        if(getCurrentProject() != null)
            setCurrentProject(getCurrentProject(), currentProjectPath);
    }

    public void startAutoSave() {
        if (currentProject != null) {
            // Créez et démarrez un nouveau thread pour la sauvegarde automatique
            Thread autoSaveThread = new Thread(() -> {
                while (true) {
                    try {
                        // Effectuez la sauvegarde automatique
                        //currentProject.saveAs("auto_save", getCurrentProjectPath());

                        // Utilisez Platform.runLater() pour exécuter l'opération sur le thread de l'interface utilisateur
                        Platform.runLater(() -> appCommandFactory.saveProject().execute());

                        // Pause pour l'intervalle spécifié
                        Thread.sleep(autoSaveIntervalMillis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // Gérer les exceptions si nécessaire
                    }
                }
            });
            autoSaveThread.setDaemon(true); // Le thread s'exécutera en arrière-plan et se terminera lorsque le programme principal se termine
            autoSaveThread.start();
        }
    }

}
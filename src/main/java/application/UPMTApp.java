package application;

import application.appCommands.ApplicationCommandFactory;
import application.configuration.Configuration;
import application.history.HistoryManager;
import components.rootLayout.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Project;

import java.io.IOException;
import java.util.UUID;


public class UPMTApp {

    private Stage primaryStage;
    private RootLayoutController rootLayoutController;
    private ApplicationCommandFactory appCommandFactory;
    private Project currentProject;
    private String currentProjectPath;
    private UUID lastSavedCommandId;

    public UPMTApp(Stage primaryStage) throws IOException {


        this.primaryStage = primaryStage;
        this.appCommandFactory = new ApplicationCommandFactory(this);
        this.rootLayoutController = new RootLayoutController(appCommandFactory);

        Configuration.loadAppConfiguration();
        HistoryManager.init(appCommandFactory);

        Scene mainScene = new Scene(RootLayoutController.createRootLayout(rootLayoutController));
        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(event -> { appCommandFactory.closeApplication(event).execute(); });
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
        rootLayoutController.setProject(project);
    }
    public Project getCurrentProject() {
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

    private void startAutoSave() {
        if (currentProject != null) {
            Thread autoSaveThread = new Thread(() -> {
                while (true) {
                    try {
                        // Effectuez la sauvegarde automatique uniquement si le projet a été modifié
                        if (currentProject.isModified()) {
                            currentProject.saveAs("auto_save", getCurrentProjectPath());
                            System.out.println(getCurrentProjectPath());
                            appCommandFactory.saveProject().execute2();

                            // Marquez le projet comme non modifié après la sauvegarde
                            currentProject.setModified(false);
                        }

                        // Pause pour l'intervalle spécifié
                        Thread.sleep(2000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        // Gérer les exceptions si nécessaire
                    }
                }
            });
            autoSaveThread.setDaemon(true);
            autoSaveThread.start();
        }
    }
}
package application;

import application.Project.Models.Project;
import application.Commands.ApplicationCommandFactory;
import application.Configuration.Configuration;
import Components.RootLayout.Controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class UPMTApp {

    private Stage primaryStage;
    private RootLayoutController rootLayoutController;
    private ApplicationCommandFactory appCommandFactory;
    private Project currentProject;
    private String currentProjectPath;

    public UPMTApp(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.appCommandFactory = new ApplicationCommandFactory(this);
        this.rootLayoutController = new RootLayoutController(appCommandFactory);

        Configuration.loadAppConfiguration();
        startApp();

        primaryStage.setOnCloseRequest(event -> { appCommandFactory.closeApplication().execute(); });

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/MainView/MainView.fxml"));
        loader.setResources(Configuration.langBundle);
        loader.setClassLoader(getClass().getClassLoader());
        loader.load();
    }

    private void startApp() {
        Scene mainScene = new Scene(RootLayoutController.createRootLayout(rootLayoutController));
        primaryStage.setTitle("uPMT");
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
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

    public void restartApp() {
        startApp();
        if(getCurrentProject() != null)
            setCurrentProject(getCurrentProject(), currentProjectPath);
    }
}
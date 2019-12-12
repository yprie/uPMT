package application.Commands;

import application.UPMTApp;
import java.util.Locale;

public class ApplicationCommandFactory {

    UPMTApp application;

    public ApplicationCommandFactory(UPMTApp app) {
        this.application = app;
    }

    public CloseApplicationCommand closeApplication() { return new CloseApplicationCommand(application); }
    public ChangeApplicationTitleCommand changeApplicationTitle(String newTitle) { return new ChangeApplicationTitleCommand(application, newTitle); }
    public NewProjectCommand newProject() { return new NewProjectCommand(application); }
    public OpenProjectFromFileCommand openProject() { return new OpenProjectFromFileCommand(application); }
    public OpenRecentProjectCommand openRecentProject(String path) { return new OpenRecentProjectCommand(application, path); }
    public SaveProjectCommand saveProject() { return new SaveProjectCommand(application); }
    public SaveProjectAsCommand saveProjectAs() { return new SaveProjectAsCommand(application); }
    public ChangeLanguageCommand changeLanguage(Locale locale) { return new ChangeLanguageCommand(application, locale); }
    public ProjectSavingStatusChangedCommand projectSavingStatusChanged() { return new ProjectSavingStatusChangedCommand(application); }
}

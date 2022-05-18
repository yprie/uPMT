package application.appCommands;

import application.UPMTApp;
import models.Interview;
import components.interviewSelector.modelCommands.AddInterviewCommand;
import utils.scrollOnDragPane.ScrollOnDragPane;
import javafx.stage.WindowEvent;

import java.util.Locale;

public class ApplicationCommandFactory {

    UPMTApp application;

    public ApplicationCommandFactory(UPMTApp app) {
        this.application = app;
    }

    public CloseApplicationCommand closeApplication() { return new CloseApplicationCommand(this, application); }
    public ChangeApplicationTitleCommand changeApplicationTitle(String newTitle) { return new ChangeApplicationTitleCommand(application, newTitle); }
    public OpenProjectManagerCommand openProjectManagerCommand() { return new OpenProjectManagerCommand(application); }
    public NewProjectCommand newProject() { return new NewProjectCommand(application); }
    public OpenProjectFromFileCommand openProject() { return new OpenProjectFromFileCommand(application); }
    public OpenRecentProjectCommand openRecentProject(String path) { return new OpenRecentProjectCommand(application, path); }
    public SaveProjectCommand saveProject() { return new SaveProjectCommand(application); }
    public SaveProjectAsCommand saveProjectAs() { return new SaveProjectAsCommand(application); }
    public ChangeLanguageCommand changeLanguage(Locale locale) { return new ChangeLanguageCommand(application, locale); }
    public ProjectSavingStatusChangedCommand projectSavingStatusChanged() { return new ProjectSavingStatusChangedCommand(application); }
    public AddInterviewCommand addInterview(Interview interview) { return new AddInterviewCommand(application.getCurrentProject(), interview); }
    public ExportToCSVCommand exportToCSV() {return new ExportToCSVCommand(application);}
    public SetAutoScrollWhenRevealCommand SetAutoScrollWhenReveal(boolean autoScrollWhenReveal) {return new SetAutoScrollWhenRevealCommand(application, autoScrollWhenReveal);}
    public CollapseAllMoments collapseAllMoments(boolean collapse) { return new CollapseAllMoments(application, collapse); }
    public ExportAsPngCommand exportAsPng(ScrollOnDragPane pane){ return new ExportAsPngCommand(application, pane); }
}

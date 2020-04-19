package application.appCommands;

import application.UPMTApp;
import application.configuration.Configuration;
import javafx.stage.FileChooser;
import persistency.Export.ProjectExporter;
import persistency.Export.exportModel;

import java.io.File;

public class ExportToCSVCommand extends ApplicationCommand<Void> {

    public ExportToCSVCommand(UPMTApp application) {
        super(application);
    }

    @Override
    public Void execute() {
        String path = Configuration.getProjectsPath()[0];
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(path).getParentFile());
        fileChooser.setInitialFileName(upmtApp.getCurrentProject().getName());
        fileChooser.setTitle(Configuration.langBundle.getString("export_to_csv"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File saveFile = fileChooser.showSaveDialog(upmtApp.getPrimaryStage());
        if(saveFile != null) {
            ProjectExporter.write(saveFile, new exportModel(upmtApp.getCurrentProject()), ProjectExporter.SEMICOLON_SEPARATOR);
        }
        return null;
    }
}

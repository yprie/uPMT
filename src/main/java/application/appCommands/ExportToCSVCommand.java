package application.appCommands;

import application.UPMTApp;
import application.configuration.Configuration;
import application.project.controllers.NewProjectController;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import persistency.Export.CSVTableModel;
import persistency.Export.CSVWriter;
import persistency.Export.ProjectTableModel;
import persistency.ProjectExporter;
import utils.DialogState;

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
            CSVWriter.write(saveFile, new ProjectTableModel(upmtApp.getCurrentProject()), CSVWriter.SEMICOLON_SEPARATOR);
            //ProjectExporter.exportToCSV(upmtApp.getCurrentProject(), saveFile);
        }
        return null;
    }
}

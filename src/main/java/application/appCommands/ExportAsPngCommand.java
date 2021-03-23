package application.appCommands;

import application.UPMTApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import utils.scrollOnDragPane.ScrollOnDragPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ExportAsPngCommand extends ApplicationCommand<Void>{

    UPMTApp application;
    private @FXML ScrollOnDragPane pane;

    public ExportAsPngCommand(UPMTApp application, ScrollOnDragPane pane){
        super(application);
        this.application = application;
        this.pane = pane;
    }

    @Override
    public Void execute(){
        try {
            System.out.println("Test de la méthode snapshot");
            WritableImage image = this.pane.snapshot(new SnapshotParameters(), null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showSaveDialog(application.getPrimaryStage());
            if (selectedFile != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

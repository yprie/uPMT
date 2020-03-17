package components.modelisationSpace.justification.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.justification.appCommands.JustificationCommandFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import models.Descripteme;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ShiftController implements Initializable {
    private Descripteme descripteme;
    private JustificationCommandFactory factory;
    private String side;

    @FXML
    Button leftButton;

    @FXML
    Button rightButton;

    public ShiftController(Descripteme descripteme, JustificationCommandFactory factory, String side) {
        this.descripteme = descripteme;
        this.factory = factory;
        this.side = side;
    }

    public static Node createShiftController(ShiftController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/Justification/Shift.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftButton.setText("<");
        leftButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  onLeft(event);
              }
        });

        rightButton.setText(">");
        rightButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onRight(event);
            }
        });
    }

    private void onLeft(MouseEvent event) {
        if (side == "left")
            factory.modifyDescripteme(descripteme, descripteme.getStartIndex()-1, descripteme.getEndIndex()).execute();

        if (side == "right")
            factory.modifyDescripteme(descripteme, descripteme.getStartIndex(), descripteme.getEndIndex()-1).execute();
    }

    private void onRight(MouseEvent event) {
        if (side == "left")
            factory.modifyDescripteme(descripteme, descripteme.getStartIndex()+1, descripteme.getEndIndex()).execute();

        if (side == "right")
            factory.modifyDescripteme(descripteme, descripteme.getStartIndex(), descripteme.getEndIndex()+1).execute();
    }
}

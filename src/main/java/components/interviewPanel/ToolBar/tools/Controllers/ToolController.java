package components.interviewPanel.ToolBar.tools.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ToolBar.tools.Tool;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class ToolController implements Initializable {
    protected final String name;
    protected final Tool tool;
    protected final SimpleBooleanProperty selectedProperty;

    @FXML protected Button button;

    public ToolController(String name, Tool tool) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty();
    }

    public ToolController(String name, Tool tool, boolean selected) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty(selected);
    }

    public static Node createToolController(ToolController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/InterviewPanel/Tool.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setText(resources.getString(name)); // Configuration.langBundle.getString(name)

        selectedProperty.addListener(change -> {
            updateStyle();
        });

        button.setOnMouseClicked(event -> {
            if (!selectedProperty.get()) {
                setIsSelected(true);
            }
        });

        Platform.runLater(this::updateStyle);
    }

    protected abstract void updateStyle();

    public BooleanProperty getSelectedProperty() {
        return selectedProperty;
    }

    public void setIsSelected(boolean newValue) {
        selectedProperty.set(newValue);
    }

    public Tool getTool() {
        return tool;
    }
}

package components.interviewPanel.ToolBar.tools.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ToolBar.tools.Tool;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;

public abstract class ToolController extends Button {
    protected final String name;
    protected final Tool tool;
    protected final SimpleBooleanProperty selectedProperty;

    public ToolController(String name, Tool tool) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty();
        initialize();
    }

    public ToolController(String name, Tool tool, boolean selected) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty(selected);
        initialize();
    }

    private void initialize() {
        String name = Configuration.langBundle.getString(this.name).substring(0, 1).toUpperCase() + Configuration.langBundle.getString(this.name).substring(1);
        setText(name);
        selectedProperty.addListener(change -> updateStyle());
        setOnMouseClicked(event -> {
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

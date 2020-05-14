package components.interviewPanel.ToolBar.tools.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ToolBar.tools.Tool;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class ToolController extends Node {
    protected final String name;
    protected final Tool tool;
    protected final SimpleBooleanProperty selectedProperty;

    protected GraphicsContext gc;

    protected Label label;
    protected VBox vbox;

    protected ToolController(String name, Tool tool) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty();
        initializeController();
    }

    public ToolController(String name, Tool tool, boolean selected) {
        this.name = name;
        this.tool = tool;
        selectedProperty = new SimpleBooleanProperty(selected);
        initializeController();
    }

    protected void initializeController() {
        label = new Label();
        vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        String name = Configuration.langBundle.getString(this.name).substring(0, 1).toUpperCase() + Configuration.langBundle.getString(this.name).substring(1);
        label.setText(name);
        selectedProperty.addListener(change -> updateStyle());
        vbox.setOnMouseClicked(event -> {
            if (!selectedProperty.get()) {
                setIsSelected(true);
            }
        });

        Canvas canvas = new Canvas(30, 20);
        gc = canvas.getGraphicsContext2D();
        vbox.getChildren().add(canvas);
        vbox.getChildren().add(label);

        initializeGraphic();
        Platform.runLater(this::updateStyle);
    }

    protected void updateStyle() {
        if (selectedProperty.get()) {
            label.setStyle("-fx-font-weight: bold");
            setSelectedGraphic();
        }
        else {
            label.setStyle("");
            label.setText(label.getText().toLowerCase());
            initializeGraphic();
        }
    }

    public Node getNode() {
        return vbox;
    }

    public BooleanProperty getSelectedProperty() {
        return selectedProperty;
    }

    public void setIsSelected(boolean newValue) {
        selectedProperty.set(newValue);
    }

    public Tool getTool() {
        return tool;
    }

    abstract void initializeGraphic();

    void setSelectedGraphic() {
        label.setText(label.getText().toUpperCase());
        gc.setLineWidth(4);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0, 0, 30, 0);
        gc.strokeLine(0, 20, 30, 20);
        gc.strokeLine(0, 0, 0, 20);
        gc.strokeLine(30, 0, 30, 20);
    }
}

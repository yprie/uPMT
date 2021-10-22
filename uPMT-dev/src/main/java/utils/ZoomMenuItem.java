package utils;

import application.configuration.AppSettings;
import application.configuration.Configuration;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ZoomMenuItem extends CustomMenuItem {
    private HBox box;
    private Label zoomLabel;
    private Label zoomTextLabel;

    public ZoomMenuItem() {
        this.setHideOnClick(false);
        box = new HBox();


        zoomTextLabel = new Label(Configuration.langBundle.getString("adjust_zoom_level"));
        zoomTextLabel.setTextFill(Color.BLACK);

        Button minusButton = new Button("-");
        minusButton.setOnAction(event -> {
            if (AppSettings.zoomLevelProperty.get() >= 10) {
                AppSettings.zoomLevelProperty.set(AppSettings.zoomLevelProperty.get() - 10);
            }
        });
        // TODO: create a command
        Button plusButton = new Button("+");
        plusButton.setOnAction(event -> {
            if (AppSettings.zoomLevelProperty.get() <= 190) {
                AppSettings.zoomLevelProperty.set(AppSettings.zoomLevelProperty.get() + 10);
            }
        });

        zoomLabel = new Label(AppSettings.zoomLevelProperty.getValue().toString());


        box.getChildren().add(zoomTextLabel);
        box.getChildren().add(minusButton);
        box.getChildren().add(zoomLabel);
        box.getChildren().add(plusButton);

        AppSettings.zoomLevelProperty.addListener((l) -> {
            zoomLabel.setText(AppSettings.zoomLevelProperty.getValue().toString());

            minusButton.setDisable(AppSettings.zoomLevelProperty.getValue() == 10);
            plusButton.setDisable(AppSettings.zoomLevelProperty.getValue() == 200);
        });
        // TODO: unbind

        this.setContent(box);
    }
}

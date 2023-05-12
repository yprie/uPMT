package components.interviewPanel.ToolBar.tools.Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PoliceSizeController {
    final String increaseIcon = "/images/increase-font.png";
    final String decreaseIcon = "/images/decrease-font.png";
    final String increaseIconDisabled = "/images/increase-font-disabled.png";
    final String decreaseIconDisabled = "/images/decrease-font-disabled.png";
    private VBox vbox;
    private Canvas canvas;
    private GraphicsContext gc;
    private SimpleIntegerProperty fontSize;
    private ObservableList<Node> hboxChildrens;
    private FontAction fontAction;

    public enum FontAction {
        INCREASE,
        DECREASE
    }

    public PoliceSizeController(ObservableList<Node> hboxChildrens, SimpleIntegerProperty fontSize, FontAction fontAction) {
        this.hboxChildrens = hboxChildrens;
        vbox = new VBox();
        canvas = new Canvas(35, 35);
        this.fontAction = fontAction;
        this.fontSize = fontSize;
        this.initGraphic();
    }

    public void initGraphic() {
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(3));
        gc = canvas.getGraphicsContext2D();
        vbox.getChildren().add(canvas);
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, 35, 35);
        if (this.fontAction.equals(FontAction.INCREASE)) {
            gc.drawImage(new Image(increaseIcon), 0, 0, 35, 35);
        } else {
            gc.drawImage(new Image(decreaseIconDisabled), 0, 0, 35, 35);
        }

        vbox.setOnMouseClicked(
                (e) -> {
                    if (this.fontAction.equals(FontAction.INCREASE)) {
                        fontSize.set(fontSize.get() + 1);
                    } else {
                        if (fontSize.get() > 1) {
                            fontSize.set(fontSize.get() - 1);
                        }

                    }
                });
        this.fontSize.addListener((obs, oldValue, newValue) -> {
            if (this.fontAction.equals(FontAction.DECREASE)) {
                if (newValue.intValue() <= 1) {
                    this.gc.drawImage(new Image(decreaseIconDisabled), 0, 0, 35, 35);
                }else{
                    this.gc.drawImage(new Image(decreaseIcon), 0, 0, 35, 35);
                }
            }
        });
        hboxChildrens.add(vbox);
    }

}

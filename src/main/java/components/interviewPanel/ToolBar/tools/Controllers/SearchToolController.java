package components.interviewPanel.ToolBar.tools.Controllers;

import application.configuration.Configuration;
import components.interviewPanel.ToolBar.tools.Tool;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SearchToolController {
    final String iconPath = "/images/book.png";
    private Label label;
    private VBox vbox;
    private Canvas canvas;
    private GraphicsContext gc;
    private SimpleBooleanProperty isSearchClicked;
    private ObservableList<Node> hboxChildrens;

    public SearchToolController(ObservableList<Node> hboxChildrens, SimpleBooleanProperty isSearchClicked) {
        this.hboxChildrens = hboxChildrens;
        label = new Label();
        vbox = new VBox();
        canvas = new Canvas(20, 20);
        this.isSearchClicked = isSearchClicked;
        this.initGraphic();
    }

    public void initGraphic() {
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(3));
        label.setText(Configuration.langBundle.getString("find"));
        gc = canvas.getGraphicsContext2D();
        vbox.getChildren().add(canvas);
        vbox.getChildren().add(label);
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, 20, 20);
        gc.drawImage(new Image(iconPath), 0, 0, 20, 20);
        vbox.setOnMouseClicked((e) ->
        {
            isSearchClicked.set(true);
        });
        hboxChildrens.add(vbox);
    }

}

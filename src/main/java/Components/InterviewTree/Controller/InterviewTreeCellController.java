package Components.InterviewTree.Controller;

import Components.InterviewTree.InterviewTreePluggable;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import utils.ResourceLoader;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class InterviewTreeCellController implements Initializable {

    @FXML
    BorderPane nameDisplayer;

    @FXML
    Label name;

    @FXML
    MenuButton optionsMenu;


    @FXML
    ImageView imageIcon;

    protected InterviewTreePluggable element;
    private boolean shouldRemoveMenuButtonVisibility;

    public InterviewTreeCellController(InterviewTreePluggable element) {
        this.element = element;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(element.nameProperty().get());
        this.name.textProperty().bind(element.nameProperty());
        imageIcon.setImage(ResourceLoader.loadImage(element.getIconPath()));
    }



    public void setOnHover(boolean YoN) {
        if(optionsMenu.isShowing()) {
            shouldRemoveMenuButtonVisibility = true;
        }
        else {
            optionsMenu.setVisible(YoN);
        }
    }

    public boolean getOnHover() {
        return optionsMenu.isVisible();
    }
}

package components.modelisationSpace.controllers;

import components.modelisationSpace.UI.AutoSuggestionsTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utils.ResourceLoader;
import utils.autoSuggestion.strategies.SuggestionStrategyCategory;
import utils.autoSuggestion.strategies.SuggestionStrategyNoSense;
import utils.scrollOnDragPane.ScrollOnDragPane;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    private  @FXML ImageView fake_view;
    private @FXML AnchorPane mainAnchorPane;

    public ModelisationSpaceController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modelisationSpace/ModelisationSpace.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        fake_view.setImage(ResourceLoader.loadImage("fake_modelisation.png"));
        AutoSuggestionsTextField autoSuggestionsTextField = new AutoSuggestionsTextField(new SuggestionStrategyCategory());
        //autoSuggestionsTextField.setStrategy(new SuggestionStrategyFolder());
        autoSuggestionsTextField.setStrategy(new SuggestionStrategyNoSense());
        mainAnchorPane.getChildren().add(autoSuggestionsTextField);
    }
}

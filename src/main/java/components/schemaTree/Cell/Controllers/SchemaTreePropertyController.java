package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import models.SchemaProperty;
import javafx.scene.control.MenuItem;
import utils.autoSuggestion.strategies.SuggestionStrategy;
import utils.autoSuggestion.strategies.SuggestionStrategyProperty;

import java.net.URL;
import java.util.ResourceBundle;


public class SchemaTreePropertyController extends SchemaTreeCellController {

    private SchemaProperty property;
    private SchemaTreeCommandFactory cmdFactory;

    public SchemaTreePropertyController(SchemaProperty property, SchemaTreeCommandFactory cmdFactory) {
        super(property);
        this.property = property;
        this.cmdFactory = cmdFactory;
    }

    @Override
    protected SuggestionStrategy getSuggestionStrategy() {
        return new SuggestionStrategyProperty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeTreeElement(property).execute();
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

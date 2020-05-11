package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import javafx.beans.binding.Bindings;
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

        name.textProperty().bind(element.nameProperty());
        complementaryInfo.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int nUses = property.numberOfUsesInModelisationProperty().get();
            if(nUses > 0) {
                s += nUses + " ";
                s += Configuration.langBundle.getString(nUses == 1 ? "filled" : "filled_plural");
            }
            return s;
        }, property.numberOfUsesInModelisationProperty()));

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeTreeElement(property).execute();
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

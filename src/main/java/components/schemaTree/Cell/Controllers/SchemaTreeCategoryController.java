package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import javafx.beans.binding.Bindings;
import models.SchemaCategory;
import models.SchemaProperty;
import javafx.scene.control.MenuItem;
import utils.autoSuggestion.strategies.SuggestionStrategy;
import utils.autoSuggestion.strategies.SuggestionStrategyCategory;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeCategoryController extends SchemaTreeCellController {

    private SchemaCategory category;
    private SchemaTreeCommandFactory cmdFactory;

    public SchemaTreeCategoryController(SchemaCategory model, SchemaTreeCommandFactory cmdFactory) {
        super(model, cmdFactory);
        this.category = model;
        this.cmdFactory = cmdFactory;
    }

    @Override
    protected SuggestionStrategy getSuggestionStrategy() {
        return new SuggestionStrategyCategory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        name.textProperty().bind(element.nameProperty());
        complementaryInfo.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int nUses = category.numberOfUsesInModelisationProperty().get();
            if(nUses > 0) {
                s += nUses + " ";
                s += Configuration.langBundle.getString(nUses == 1 ? "use" : "uses");
            }
            return s;
        }, category.numberOfUsesInModelisationProperty()));

        MenuItem addPropertyButton = new MenuItem(Configuration.langBundle.getString("add_property"));
        addPropertyButton.setOnAction(actionEvent -> {
            SchemaProperty p = new SchemaProperty(Configuration.langBundle.getString("property"));
            cmdFactory.addSchemaTreeChild(p).execute();
        });
        optionsMenu.getItems().add(addPropertyButton);

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeTreeElement(category).execute();
        });
        optionsMenu.getItems().add(deleteButton);
    }
}

package components.schemaTree.Cell.Controllers;

import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import models.SchemaMomentType;
import utils.autoSuggestion.strategies.SuggestionStrategy;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeMomentTypeController extends SchemaTreeCellController {

    private SchemaMomentType momentType;
    private SchemaTreeCommandFactory cmdFactory;

    public SchemaTreeMomentTypeController(SchemaMomentType momentType, SchemaTreeCommandFactory cmdFactory) {
        super(momentType, cmdFactory);
        this.momentType = momentType;
        this.cmdFactory = cmdFactory;
    }

    @Override
    protected SuggestionStrategy getSuggestionStrategy() {
        return new SuggestionStrategyMoment();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        name.textProperty().bind(this.momentType.nameProperty());
    }
}

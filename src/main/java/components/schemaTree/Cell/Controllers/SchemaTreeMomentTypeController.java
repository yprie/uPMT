package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.toolbox.controllers.ToolBoxControllers;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import models.SchemaMomentType;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategy;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeMomentTypeController extends SchemaTreeCellController {

    private SchemaMomentType schemaMomentType;
    private SchemaTreeCommandFactory cmdFactory;
    private boolean renamingMode = false;

    public SchemaTreeMomentTypeController(SchemaMomentType schemaMomentType, SchemaTreeCommandFactory cmdFactory) {
        super(schemaMomentType, cmdFactory);
        this.schemaMomentType = schemaMomentType;
        this.cmdFactory = cmdFactory;
    }

    @Override
    protected SuggestionStrategy getSuggestionStrategy() {
        return new SuggestionStrategyMoment();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        name.textProperty().bind(this.schemaMomentType.nameProperty());

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            ToolBoxControllers.getToolBoxControllersInstance().removeMomentTypeCommand(schemaMomentType);
        });
        optionsMenu.getItems().add(deleteButton);

        MenuItem showButton = new MenuItem("Hide");
        showButton.setOnAction(actionEvent -> {
            if (showButton.textProperty().get().equals("Hide")) {
                ToolBoxControllers.getToolBoxControllersInstance().hide(schemaMomentType.getMomentTypeController());
                showButton.textProperty().set("Show");
            } else {
                ToolBoxControllers.getToolBoxControllersInstance().show(schemaMomentType.getMomentTypeController());
                showButton.textProperty().set("Hide");
            }
        });
        optionsMenu.getItems().add(showButton);
    }

    @Override
    public void passInRenamingMode(boolean YoN) {
        if (YoN != renamingMode) {
            if (YoN) {
                renamingField = new AutoSuggestionsTextField(name.getText(), new SuggestionStrategyMoment());
                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (renamingField.getLength() > 0) {
                            cmdFactory.renameTreeSchemaMomentTypes(this.schemaMomentType, renamingField.getText()).execute();
                        }
                        passInRenamingMode(false);
                    }
                });

                this.nameDisplayer.setLeft(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            } else {
                this.nameDisplayer.setLeft(name);
                renamingMode = false;
            }
        }
    }
}

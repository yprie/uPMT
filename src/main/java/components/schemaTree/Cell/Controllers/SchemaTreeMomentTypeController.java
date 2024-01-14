package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.toolbox.controllers.ToolBoxControllers;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import models.SchemaCategory;
import models.SchemaMomentType;
import models.SchemaProperty;
import utils.ResourceLoader;
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
            cmdFactory.removeTreeElement(schemaMomentType).execute();
            ToolBoxControllers.getToolBoxControllersInstance().removeMomentTypeCommand(schemaMomentType);
        });
        optionsMenu.getItems().add(deleteButton);

        MenuItem showButton = new MenuItem();
        if (ToolBoxControllers.getToolBoxControllersInstance().getCurrentMomentTypeControllers().contains(this.schemaMomentType.getMomentTypeController())) {
            showButton.setText(Configuration.langBundle.getString("hide_in_toolbox"));
        }
        else {
            showButton.setText(Configuration.langBundle.getString("show_in_toolbox"));
        }

        showButton.setOnAction(actionEvent -> {
            if (ToolBoxControllers.getToolBoxControllersInstance().getCurrentMomentTypeControllers().contains(this.schemaMomentType.getMomentTypeController())) {
                ToolBoxControllers.getToolBoxControllersInstance().hide(schemaMomentType.getMomentTypeController());
                showButton.textProperty().set(Configuration.langBundle.getString("show_in_toolbox"));
            } else {
                ToolBoxControllers.getToolBoxControllersInstance().show(schemaMomentType.getMomentTypeController());
                showButton.textProperty().set(Configuration.langBundle.getString("hide_in_toolbox"));
            }
        });
        optionsMenu.getItems().add(showButton);
        updateMomentTypeIcon(schemaMomentType.getColor());
    }

    public void updateMomentTypeIcon(String color) {
        switch (color) {
            case "ffffff":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_icon.png"));
                break;
            case "b97a57":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_brown.png"));
                break;
            case "ffaec9":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_pink.png"));
                break;
            case "ffc90e":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_yellow.png"));
                break;
            case "b5e61d":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_green.png"));
                break;
            case "7092be":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_blue.png"));
                break;
            case "8671cd":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_purple.png"));
                break;
            case "f15252":
                pictureView.setImage(ResourceLoader.loadImage("toolBox_red.png"));
                break;
            default:
                pictureView.setImage(ResourceLoader.loadImage("toolBox_icon.png"));
                break;
        }
    }

    @Override
    public void setOnHover(boolean YoN) {
        super.setOnHover(YoN);

        StringBuilder message = new StringBuilder(schemaMomentType.getName() + "\n");
        for (SchemaCategory sc : schemaMomentType.categoriesProperty()) {
            message.append('\n').append(sc.getName()).append(" :\n");
            for (SchemaProperty sp : sc.propertiesProperty()) {
                message.append("\t- ").append(sp.getName()).append("\n");
            }
        }

        Tooltip tt = new Tooltip(message.toString());
        tt.setShowDelay(Duration.millis(500));
        Tooltip.install(nameDisplayer, tt);
    }

    @Override
    public void passInRenamingMode(boolean YoN, boolean deleteIfUnavailable) {
        if (YoN != renamingMode) {
            if (YoN) {
                renamingField = new AutoSuggestionsTextField(name.getText(), new SuggestionStrategyMoment());
                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false, false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (renamingField.getLength() > 0) {
                            cmdFactory.renameTreeSchemaMomentTypes(this.schemaMomentType, renamingField.getText()).execute();
                        }
                        passInRenamingMode(false, false);
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

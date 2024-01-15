package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.toolbox.controllers.ToolBoxControllers;
import javafx.geometry.Pos;
import javafx.scene.control.Menu;
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
        addColorChange();
        updateMomentTypeIcon(schemaMomentType.getColor());
    }
    private void addColorChange() {
        Menu changeColor = new Menu(Configuration.langBundle.getString("change_color"));

        MenuItem white = new MenuItem("    ");
        white.setStyle("-fx-background-color: #ffffff;\n");
        white.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "ffffff").execute();
            updateMomentTypeIcon("ffffff");
        });
        changeColor.getItems().add(white);

        MenuItem brown = new MenuItem("    ");
        brown.setStyle("-fx-background-color: #b97a57;\n");
        brown.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "b97a57").execute();
            updateMomentTypeIcon("b97a57");
        });
        changeColor.getItems().add(brown);

        MenuItem pink = new MenuItem("    ");
        pink.setStyle("-fx-background-color: #ffaec9;\n");
        pink.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "ffaec9").execute();
            updateMomentTypeIcon("ffaec9");
        });
        changeColor.getItems().add(pink);

        MenuItem yellow = new MenuItem("    ");
        yellow.setStyle("-fx-background-color: #ffc90e;\n");
        yellow.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "ffc90e").execute();
            updateMomentTypeIcon("ffc90e");
        });
        changeColor.getItems().add(yellow);

        MenuItem green = new MenuItem("    ");
        green.setStyle("-fx-background-color: #b5e61d;\n");
        green.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "b5e61d").execute();
            updateMomentTypeIcon("b5e61d");
        });
        changeColor.getItems().add(green);

        MenuItem blue = new MenuItem("    ");
        blue.setStyle("-fx-background-color: #7092be;\n");
        blue.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "7092be").execute();
            updateMomentTypeIcon("7092be");
        });
        changeColor.getItems().add(blue);

        MenuItem purple = new MenuItem("    ");
        purple.setStyle("-fx-background-color: #8671cd;\n");
        purple.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "8671cd").execute();
            updateMomentTypeIcon("8671cd");
        });
        changeColor.getItems().add(purple);

        MenuItem red = new MenuItem("    ");
        red.setStyle("-fx-background-color: #f15252;\n");
        red.setOnAction(actionEvent -> {
            cmdFactory.colorCommandMomentType(schemaMomentType, "f15252").execute();
            updateMomentTypeIcon("f15252");
        });
        changeColor.getItems().add(red);

        optionsMenu.getItems().add(changeColor);
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

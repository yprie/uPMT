package components.toolbox.controllers;

import application.configuration.Configuration;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import components.toolbox.appCommand.RenameMomentTypesCommand;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import models.SchemaMomentType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import models.Moment;
import models.*;
import utils.ResourceLoader;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.dragAndDrop.DragStore;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MomentTypeController implements Initializable {
    private @FXML BorderPane momentTypeBorderPane;
    private @FXML Label momentTypeLabel;
    @FXML
    MenuButton optionsMenu;
    @FXML
    ImageView pictureView;
    private SchemaMomentType schemaMomentType;

    private boolean renamingMode = false;
    private AutoSuggestionsTextField renamingField;
    private SchemaTreeCommandFactory cmdFactory;
    public MomentTypeController(Moment moment ) {
        this.schemaMomentType = new SchemaMomentType(moment, this);
    }

    public MomentTypeController(SchemaMomentType schemaMomentType) {
        this.schemaMomentType = schemaMomentType;
    }

//    public MomentTypeController(SchemaMomentType schemaMomentType, SchemaTreeCommandFactory cmdFactory) {
//        this.schemaMomentType = schemaMomentType;
//        this.cmdFactory =cmdFactory;
//    }
    public static Node createMomentTypeController(MomentTypeController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/Toolbox/components/MomentType.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.momentTypeLabel.textProperty().bind(this.schemaMomentType.nameProperty());
        this.momentTypeBorderPane.setStyle("-fx-background-color: #"+this.schemaMomentType.getColor()+";");
        setupDragAndDrop();
        updatePopUp();
        setupContextMenu();

        schemaMomentType.nameProperty().addListener((observableValue, o, t1) -> updatePopUp());
        schemaMomentType.categoriesProperty().addListener((observableValue, o, t1) -> updatePopUp());
        addColorChange();
    }

    private void setupDragAndDrop() {
       momentTypeBorderPane.setOnDragDetected(event -> {
           Dragboard db = momentTypeBorderPane.startDragAndDrop(TransferMode.MOVE);
           ClipboardContent content = new ClipboardContent();
           content.put(SchemaMomentType.format, 0);
           DragStore.setDraggable(schemaMomentType);
           db.setContent(content);
           event.consume();
       });
    }
    private void setupContextMenu(){
        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> {
            passInRenamingMode(true);
        });
        optionsMenu.getItems().add(renameButton);
        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            ToolBoxControllers.getToolBoxControllersInstance().removeMomentTypeCommand(schemaMomentType);
        });
        optionsMenu.getItems().add(deleteButton);
        this.momentTypeBorderPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    passInRenamingMode(true);
                }
            }
        });
    }

    private void updatePopUp() {
        StringBuilder message = new StringBuilder(schemaMomentType.getName() + "\n");
        for (SchemaCategory sc : schemaMomentType.categoriesProperty()) {
            message.append('\n').append(sc.getName()).append(" :\n");
            for (SchemaProperty sp : sc.propertiesProperty()) {
                message.append("\t- ").append(sp.getName()).append("\n");
            }
        }

        Tooltip tt = new Tooltip(message.toString());
        tt.setShowDelay(Duration.millis(500));
        Tooltip.install(momentTypeBorderPane, tt);
    }

    public boolean exists(Moment moment) {
        return moment.getName().equals(this.schemaMomentType.getName());
    }

    public SchemaMomentType getSchemaMomentType() {
        return schemaMomentType;
    }

    public void passInRenamingMode(boolean YoN) {
        if (YoN != renamingMode) {
            if (YoN) {
                renamingField = new AutoSuggestionsTextField(schemaMomentType.getName(), new SuggestionStrategyMoment());
                renamingField.setAlignment(Pos.BASELINE_CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (renamingField.getLength() > 0) {
                            new RenameMomentTypesCommand(this.schemaMomentType, renamingField.getText()).execute();
                        }
                        passInRenamingMode(false);
                    }
                });

                this.momentTypeBorderPane.setCenter(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            } else {
                this.momentTypeBorderPane.setCenter(momentTypeLabel);
                renamingMode = false;
            }
        }
    }
    public void updateColor() {
        momentTypeBorderPane.setStyle("-fx-background-color: #" + schemaMomentType.getColor() + ";\n");
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
            if (cmdFactory == null) {
                System.out.println("cmdFactory is null");
            }
            if (schemaMomentType == null) {
                System.out.println("schemaMomentType is null");
            }
            if (cmdFactory != null && schemaMomentType != null) {
                cmdFactory.colorCommandMomentType(schemaMomentType, "7092be");
                if (cmdFactory.colorCommandMomentType(schemaMomentType, "7092be") != null) {
                    cmdFactory.colorCommandMomentType(schemaMomentType, "7092be").execute();
                } else {
                    System.out.println("colorCommandMomentType returns null");
                }
            }
//            cmdFactory.colorCommandMomentType(schemaMomentType, "b97a57").execute();
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
}

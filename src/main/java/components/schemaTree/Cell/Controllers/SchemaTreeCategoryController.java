package components.schemaTree.Cell.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.appCommands.SchemaTreeCommandFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import models.ConcreteCategory;
import models.Moment;
import models.SchemaCategory;
import models.SchemaProperty;
import utils.GlobalVariables;
import utils.ResourceLoader;
import utils.autoSuggestion.strategies.SuggestionStrategy;
import utils.autoSuggestion.strategies.SuggestionStrategyCategory;

import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeCategoryController extends SchemaTreeCellController {

    private SchemaCategory category;
    private SchemaTreeCommandFactory cmdFactory;
    private GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();
    private SimpleIntegerProperty currentNavigationIndex = new SimpleIntegerProperty(-1);
    @FXML
    HBox navigationBox;
    @FXML
    BorderPane usesSection;
    private Button leftNavigation;
    private Button rightNavigation;
    private Tooltip rightNavigationButtonTooltip;
    private Tooltip leftNavigationButtonTooltip;

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
        this.leftNavigation = new Button("<");
        this.rightNavigation = new Button(">");
        this.leftNavigation.setPadding(new Insets(1, 3, 1, 3));
        this.rightNavigation.setPadding(new Insets(1, 3, 1, 3));
        this.leftNavigation.setStyle("-fx-background-color:rgba(0, 0, 0, 0.1);-fx-background-size:0;");
        this.rightNavigation.setStyle("-fx-background-color:rgba(0, 0, 0, 0.1);-fx-background-size:0;");
        this.navigationBox.setSpacing(5);
        this.navigationBox.getChildren().add(this.leftNavigation);
        this.navigationBox.getChildren().add(this.rightNavigation);
        this.navigationBox.setPadding(new Insets(7.5, 0, 0, 0));

        this.complementaryInfoTooltip = new Tooltip(Configuration.langBundle.getString("complementary_info_toolbox"));
//        this.complementaryInfoTooltip = new Tooltip("Navigate to nb : "+(Math.floorMod(this.currentNavigationIndex - 1, this.category.getCurrentInterviewUses())+1));

        complementaryInfoTooltip.setShowDelay(new Duration(0));
        this.complementaryInfo.setTooltip(complementaryInfoTooltip);
        this.usesPerInterview.setTooltip(complementaryInfoTooltip);
        name.textProperty().bind(element.nameProperty());
        usesPerInterview.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int currentInterviewUses = this.category.getCurrentInterviewUses();
            int nUsesInModelisation = category.numberOfUsesInModelisationProperty().get();
            if (nUsesInModelisation > 0) {
                s = " (" + this.category.getCurrentInterviewUses() + " /";
            }
            return s;

        }, this.category.currentInterviewUsesProperty()));
        complementaryInfo.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int nUses = category.numberOfUsesInModelisationProperty().get();
            if (nUses > 0) {
                s += nUses + " ";
                s += Configuration.langBundle.getString(nUses == 1 ? "use" : "uses");
                s += ")";
            }
            return s;
        }, category.numberOfUsesInModelisationProperty()));


        MenuItem addPropertyButton = new MenuItem(Configuration.langBundle.getString("add_property"));
        addPropertyButton.setOnAction(actionEvent -> {
            SchemaProperty p = new SchemaProperty(Configuration.langBundle.getString("property"));
            cmdFactory.addSchemaTreeChild(p).execute();
        });
        optionsMenu.getItems().add(addPropertyButton);

        addColorChange();
        updateCategoryIcon(category.getColor());

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> {
            cmdFactory.removeTreeElement(category).execute();
        });
        optionsMenu.getItems().add(deleteButton);
        if (this.category.getCurrentInterviewUses() <= 0) {
            this.leftNavigation.setDisable(true);
            this.rightNavigation.setDisable(true);
        } else {
            this.leftNavigation.setDisable(false);
            this.rightNavigation.setDisable(false);
        }
        this.category.currentInterviewUsesProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.intValue() <= 0) {
                this.leftNavigation.setDisable(true);
                this.rightNavigation.setDisable(true);
            } else {
                this.leftNavigation.setDisable(false);
                this.rightNavigation.setDisable(false);
            }
        });

        //Navigation Buttons ToolTipSetup
        this.setupNavigationToolTip();
        this.setupNavigationHandlers();


    }

    private void addColorChange() {
        Menu changeColor = new Menu(Configuration.langBundle.getString("change_color"));

        MenuItem white = new MenuItem("    ");
        white.setStyle("-fx-background-color: #ffffff;\n");
        white.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "ffffff").execute();
            updateCategoryIcon("ffffff");
        });
        changeColor.getItems().add(white);

        MenuItem brown = new MenuItem("    ");
        brown.setStyle("-fx-background-color: #c9a18b;\n");
        brown.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "c9a18b").execute();
            updateCategoryIcon("c9a18b");
        });
        changeColor.getItems().add(brown);

        MenuItem pink = new MenuItem("    ");
        pink.setStyle("-fx-background-color: #ffd7e4;\n");
        pink.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "ffd7e4").execute();
            updateCategoryIcon("ffd7e4");
        });
        changeColor.getItems().add(pink);

        MenuItem yellow = new MenuItem("    ");
        yellow.setStyle("-fx-background-color: #efe4b0;\n");
        yellow.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "efe4b0").execute();
            updateCategoryIcon("efe4b0");
        });
        changeColor.getItems().add(yellow);

        MenuItem green = new MenuItem("    ");
        green.setStyle("-fx-background-color: #d1eb81;\n");
        green.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "d1eb81").execute();
            updateCategoryIcon("d1eb81");
        });
        changeColor.getItems().add(green);

        MenuItem blue = new MenuItem("    ");
        blue.setStyle("-fx-background-color: #99d9ea;\n");
        blue.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "99d9ea").execute();
            updateCategoryIcon("99d9ea");
        });
        changeColor.getItems().add(blue);

        MenuItem purple = new MenuItem("    ");
        purple.setStyle("-fx-background-color: #9b8dcc;\n");
        purple.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "9b8dcc").execute();
            updateCategoryIcon("9b8dcc");
        });
        changeColor.getItems().add(purple);

        MenuItem red = new MenuItem("    ");
        red.setStyle("-fx-background-color: #f4a6a6;\n");
        red.setOnAction(actionEvent -> {
            cmdFactory.colorCommand(category, "f4a6a6").execute();
            updateCategoryIcon("f4a6a6");
        });
        changeColor.getItems().add(red);

        optionsMenu.getItems().add(changeColor);
    }

    public void updateCategoryIcon(String color) {

        switch (color) {
            case "ffffff":
                pictureView.setImage(ResourceLoader.loadImage("category_white_filled.png"));
                break;
            case "c9a18b":
                pictureView.setImage(ResourceLoader.loadImage("category_brown.png"));
                break;
            case "ffd7e4":
                pictureView.setImage(ResourceLoader.loadImage("category_pink.png"));
                break;
            case "efe4b0":
                pictureView.setImage(ResourceLoader.loadImage("category_yellow.png"));
                break;
            case "d1eb81":
                pictureView.setImage(ResourceLoader.loadImage("category_green.png"));
                break;
            case "99d9ea":
                pictureView.setImage(ResourceLoader.loadImage("category_blue.png"));
                break;
            case "9b8dcc":
                pictureView.setImage(ResourceLoader.loadImage("category_purple.png"));
                break;
            case "f4a6a6":
                pictureView.setImage(ResourceLoader.loadImage("category_red.png"));
                break;
            default:
                pictureView.setImage(ResourceLoader.loadImage("category.png"));
                break;
        }
    }

    public Node getNavigationNode(int index) {
        Moment navigationMoment = GlobalVariables.nodeViews.
                getInstanceByCategory(this.category, this.currentNavigationIndex.get());
        for (ConcreteCategory category : navigationMoment.getCategories()) {
            if (category.getSchemaCategory().equals(this.category)) {
                return category.getController().getName();
            }
        }
        return navigationMoment.getController().nameBox;
    }

    private int calculateLeftIndex(int currentIndex, int total) {
        if (currentIndex == -1) {
            return total - 1;
        } else {
            return Math.floorMod(currentIndex - 1, total);
        }
    }

    private int calculateRightIndex(int currentIndex, int total) {

        return Math.floorMod(currentIndex + 1, total);
    }

    private void setupNavigationToolTip() {
        this.leftNavigationButtonTooltip = new Tooltip();
        this.rightNavigationButtonTooltip = new Tooltip();
        leftNavigationButtonTooltip.setShowDelay(new Duration(100));
        rightNavigationButtonTooltip.setShowDelay(new Duration(100));
        leftNavigationButtonTooltip.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int currentInterviewUses = this.category.getCurrentInterviewUses();
            if (currentInterviewUses > 0) {
                s = Configuration.langBundle.getString("navigation_tooltip") + (this.calculateLeftIndex(this.currentNavigationIndex.get(), this.category.getCurrentInterviewUses()) + 1);
            }
            return s;

        }, this.currentNavigationIndex, this.category.currentInterviewUsesProperty()));
        rightNavigationButtonTooltip.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int currentInterviewUses = this.category.getCurrentInterviewUses();
            if (currentInterviewUses > 0) {
                s = Configuration.langBundle.getString("navigation_tooltip") + (this.calculateRightIndex(this.currentNavigationIndex.get(), this.category.getCurrentInterviewUses()) + 1);
            }
            return s;

        }, this.currentNavigationIndex, this.category.currentInterviewUsesProperty()));
        this.leftNavigation.setTooltip(leftNavigationButtonTooltip);
        this.rightNavigation.setTooltip(rightNavigationButtonTooltip);
    }

    private void setupNavigationHandlers() {
        this.leftNavigation.setOnMouseClicked((e) -> {

            this.currentNavigationIndex.set(this.calculateLeftIndex(this.currentNavigationIndex.get(), this.category.getCurrentInterviewUses()));

            GlobalVariables.modelisationNavigator.
                    centerNodeInScrollPaneX(this.getNavigationNode(this.currentNavigationIndex.get()));
            GlobalVariables.modelisationNavigator.
                    centerNodeInScrollPaneY(this.getNavigationNode(this.currentNavigationIndex.get()));
        });

        this.rightNavigation.setOnMouseClicked((e) -> {
            this.currentNavigationIndex.set(this.calculateRightIndex(this.currentNavigationIndex.get(), this.category.getCurrentInterviewUses()));

            GlobalVariables.modelisationNavigator.
                    centerNodeInScrollPaneX(this.getNavigationNode(this.currentNavigationIndex.get()));
            GlobalVariables.modelisationNavigator.
                    centerNodeInScrollPaneY(this.getNavigationNode(this.currentNavigationIndex.get()));
        });
    }
}

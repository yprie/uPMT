package components.modelisationSpace.controllers;

import application.configuration.AppSettings;
import application.configuration.Configuration;
import components.interviewPanel.search.ButtonSearchType;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.hooks.ModelisationSpaceHook;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.moment.controllers.RootMomentController;
import components.modelisationSpace.search.MomentSearchHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import models.RootMoment;
import models.TemplateMoment;
import utils.GlobalVariables;
import utils.ModelisationNavigator;
import utils.MomentSearch;
import utils.SearchProperties;
import utils.dragAndDrop.DragStore;
import utils.scrollOnDragPane.ScrollOnDragPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModelisationSpaceController extends ScrollOnDragPane implements Initializable {

    private ScrollPaneCommandFactory paneCmdFactory;
    private RootMomentController rmController;

    private @FXML
    AnchorPane pane;
    private @FXML
    ScrollOnDragPane superPane;

    AnchorPane anchorPane = new AnchorPane(); // Container
    private SimpleBooleanProperty isSearchClicked;
    private ModelisationSpaceHook hooks;
    private ModelisationSpaceHookNotifier hooksNotifier;
    private MomentSearchHandler nextButtonHandler;
    private MomentSearchHandler previousButtonHandler;
    GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();
    private MomentSearch searchResult;
    private SearchProperties searchProperties;

    public ModelisationSpaceController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modelisationSpace/ModelisationSpace.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        hooks = new ModelisationSpaceHook();
        hooksNotifier = new ModelisationSpaceHookNotifier(hooks);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        paneCmdFactory = new ScrollPaneCommandFactory(superPane);
        this.searchResult = new MomentSearch();
        this.searchProperties = new SearchProperties();
        setupDragAndDrop();
        this.isSearchClicked = new SimpleBooleanProperty(false);
        globalVariables.isMomentSearchClicked = this.isSearchClicked;
        this.superPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F && e.isShortcutDown()) {
                isSearchClicked.set(true);
            }
        });
        this.isSearchClicked.addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                this.superPane.requestFocus();
                showFindDialog();
            }
        });
        GlobalVariables.modelisationNavigator = new ModelisationNavigator(this.superPane, this.anchorPane);

    }

    public void setRootMoment(RootMoment m) {
        //Set new moment
        clearSpace();
        if (m != null) {
            rmController = new RootMomentController(m, paneCmdFactory, hooksNotifier);
            anchorPane.getChildren().clear();
            anchorPane.getChildren().add(RootMomentController.createRootMoment(rmController));
            superPane.setContent(anchorPane);
            double r = AppSettings.zoomLevelProperty.getValue() * 0.01;
            anchorPane.getTransforms().setAll(new Scale(r, r, 0, 0));
            AppSettings.zoomLevelProperty.addListener((l) -> {
                double ratio = AppSettings.zoomLevelProperty.getValue() * 0.01;
                anchorPane.getTransforms().setAll(new Scale(ratio, ratio, 0, 0));
            });
        }
    }

    public void clearSpace() {
        if (rmController != null)
            rmController.unmount();
        superPane.setContent(null);
    }

    public ModelisationSpaceHook getHooks() {
        return hooks;
    }

    private void setupDragAndDrop() {
        superPane.setOnDragOverHook((e) -> {
            if (
                    !rmController.hasAtLeastOneChildMoment()
                            && DragStore.getDraggable().isDraggable()
                            && !e.isAccepted()
                            && DragStore.getDraggable().getDataFormat() == TemplateMoment.format
            ) {
                e.acceptTransferModes(TransferMode.COPY);
            }
        });

        superPane.setOnDragDroppedHook(e -> {
            if (
                    !rmController.hasAtLeastOneChildMoment()
                            && e.isAccepted()
                            && DragStore.getDraggable().getDataFormat() == TemplateMoment.format
            ) {
                TemplateMoment t = DragStore.getDraggable();
                rmController.addMoment(t.createConcreteMoment());
            }
        });
    }

    public ScrollOnDragPane getSuperPane() {
        return this.superPane;
    }


    public void centerNodeInScrollPaneX(AnchorPane scrollPane, Node node) {
        double h = this.anchorPane.getBoundsInLocal().getWidth();//scrollpane
        //System.out.println(node.getBoundsInParent());
        Bounds bounds =
                this.anchorPane.sceneToLocal(node.localToScene(node.getBoundsInLocal()));//scrollpane
        //System.out.println(bounds);
        double y = (bounds.getMaxX() +
                bounds.getMinX()) / 2.0;
        double v = this.superPane.getViewportBounds().getWidth();
        this.superPane.setHvalue(this.superPane.getHmax() * ((y - 0.5 * v) / (h - v)));
    }

    public void centerNodeInScrollPaneY(ScrollPane scrollPane, Node node) {
        double h = this.anchorPane.getBoundsInLocal().getHeight();
        Bounds bounds =
                this.anchorPane.sceneToLocal(node.localToScene(node.getBoundsInLocal()));
        double y = (bounds.getMaxY() +
                bounds.getMinY()) / 2.0;
        double v = this.superPane.getViewportBounds().getHeight();
        System.out.println(node.getBoundsInParent());
        System.out.println(bounds);
        //testing two
        double height = this.anchorPane.getBoundsInLocal().getHeight();
        double y2 = node.getBoundsInParent().getMaxY();
        this.superPane.setVvalue(y2 / height);
        //this.superPane.setVvalue(this.superPane.getVmax() * ((y - 0.5 * v) / (h - v)));
    }


    private void showFindDialog() {
        // Set up the dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        dialog.getDialogPane().setPrefWidth(1000);
        dialog.setResizable(true);
        dialog.setTitle(Configuration.langBundle.getString("find_moment"));
        dialog.setHeaderText(Configuration.langBundle.getString("find_moment"));
        dialog.setResizable(false);

        // Set up the buttons
        ButtonType findPreviousButtonType = new ButtonType(Configuration.langBundle.getString("previous"), ButtonBar.ButtonData.OK_DONE);
        ButtonType findNextButtonType = new ButtonType(Configuration.langBundle.getString("next"), ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButtonType = ButtonType.CLOSE;
        dialog.getDialogPane().getButtonTypes().addAll(findNextButtonType, findPreviousButtonType, closeButtonType);

        // Set up the text field and label
        TextField findTextField = new TextField();
        Label findLabel = new Label(Configuration.langBundle.getString("find"));
        findLabel.setLabelFor(findTextField);

        // Set up the match count label
        Label matchCountLabel = new Label();
        matchCountLabel.setVisible(false);

        //Create the radios choices
        // Create a ToggleGroup to ensure that only one radio button is selected at a time
        ToggleGroup toggleGroup = new ToggleGroup();

        // Create four RadioButtons and add them to the ToggleGroup
        RadioButton moment_name_choice = new RadioButton(Configuration.langBundle.getString("moment_name"));
        moment_name_choice.setToggleGroup(toggleGroup);
        moment_name_choice.setOnAction(e -> {
            if (moment_name_choice.isSelected()) {
                this.searchProperties.setChoiceMomentName();
                if (!findTextField.getText().isEmpty()) {
                    this.launchSearch(findTextField.getText(), matchCountLabel);
                }
            }
        });

        RadioButton category_name_choice = new RadioButton(Configuration.langBundle.getString("category_name"));
        category_name_choice.setToggleGroup(toggleGroup);
        category_name_choice.setOnAction(e -> {
            if (category_name_choice.isSelected()) {
                this.searchProperties.setChoiceCategoryName();
                if (!findTextField.getText().isEmpty()) {
                    this.launchSearch(findTextField.getText(), matchCountLabel);
                }
            }
        });

        RadioButton property_choice = new RadioButton(Configuration.langBundle.getString("property_name"));
        property_choice.setToggleGroup(toggleGroup);
        property_choice.setOnAction(e -> {
            if (property_choice.isSelected()) {
                this.searchProperties.setChoiceProperty();
                if (!findTextField.getText().isEmpty()) {
                    this.launchSearch(findTextField.getText(), matchCountLabel);
                }
            }
        });

//        RadioButton property_value_choice = new RadioButton(Configuration.langBundle.getString("property_value"));
//        property_value_choice.setToggleGroup(toggleGroup);
//        property_value_choice.setOnAction(e -> {
//            if (property_value_choice.isSelected()) {
//                this.searchProperties.setChoicePropertyValue();
//                if (!findTextField.getText().isEmpty()) {
//                    this.launchSearch(findTextField.getText(), matchCountLabel);
//                }
//            }
//        });


        RadioButton all_choice = new RadioButton(Configuration.langBundle.getString("all"));
        all_choice.setToggleGroup(toggleGroup);
        all_choice.setOnAction(e -> {
            if (all_choice.isSelected()) {
                this.searchProperties.setChoiceAll();
                if (!findTextField.getText().isEmpty()) {
                    this.launchSearch(findTextField.getText(), matchCountLabel);
                }
            }
        });
        all_choice.setStyle("-fx-font-weight: bold;");

        Label choicesLabel = new Label(Configuration.langBundle.getString("search_in"));
        // Create an HBox container and add the RadioButtons to it
        HBox choicesHbox = new HBox(10); // 10 is the spacing between the controls
        choicesHbox.setPadding(new Insets(10)); // Set some padding around the container
        choicesHbox.getChildren().
                addAll(choicesLabel, moment_name_choice, category_name_choice, property_choice, all_choice);
        // Set up the grid pane
        HBox gridPane = new HBox();
        gridPane.setSpacing(15);
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.getChildren().add(findLabel);
        gridPane.getChildren().add(findTextField);
        gridPane.getChildren().add(matchCountLabel);
        gridPane.getChildren().
                addAll(choicesLabel, all_choice, moment_name_choice, category_name_choice, property_choice);
        //create vbox
//        VBox vBox = new VBox(20);
//        vBox.getChildren().add(choicesHbox);
//        vBox.getChildren().add(gridPane);

        dialog.getDialogPane().setContent(gridPane);

        Button findPreviousButton = (Button) dialog.getDialogPane().lookupButton(findPreviousButtonType);
        Button findNextButton = (Button) dialog.getDialogPane().lookupButton(findNextButtonType);
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(closeButtonType);
        //Init Buttons on disabled
        findNextButton.setDisable(true);
        findPreviousButton.setDisable(true);
        all_choice.fire();

//        handle search result label
        findTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                this.launchSearch(newText, matchCountLabel);
            } else {
                this.searchResult.resetSearch();
                matchCountLabel.setVisible(false);
            }

        });
        //If we changed the interview and the panel is open we just relaunch the search on the new one
        GlobalVariables.getGlobalVariables().getProject().selectedInterviewProperty().addListener(
                (obs, oldProject, newProject) -> {
                    if (!findTextField.getText().isEmpty()) {
                        this.launchSearch(findTextField.getText(), matchCountLabel);
                    }
                }
        );

        //Handle buttons disabled property listeners
        this.searchResult.resultCountProperty().addListener((obs, oldCount, newCount) -> {
            findNextButton.setDisable(newCount.intValue() <= 0);
        });
        this.searchResult.resultPositionProperty().addListener((obs, oldCount, newCount) -> {
            findNextButton.setDisable(!this.searchResult.hasNext());
            findPreviousButton.setDisable(!this.searchResult.hasPrevious());
        });
        //Set up search buttons actions
        this.nextButtonHandler = new MomentSearchHandler(
                ButtonSearchType.NEXT, searchResult, superPane, anchorPane);
        this.previousButtonHandler = new MomentSearchHandler(
                ButtonSearchType.PREVIOUS, searchResult, superPane, anchorPane);
        findNextButton.addEventFilter(ActionEvent.ACTION, this.nextButtonHandler);
        findPreviousButton.addEventFilter(ActionEvent.ACTION, this.previousButtonHandler);
        closeButton.addEventFilter(ActionEvent.ACTION, (e) -> {
            this.isSearchClicked.set(false);
        });
        // Show the dialog and reset the search result
        dialog.setOnCloseRequest(e -> {
            this.previousButtonHandler.cleanSearchHighlight();
            this.nextButtonHandler.cleanSearchHighlight();
            this.searchResult.resetSearch();
        });
        dialog.show();
        dialog.getDialogPane().toFront();
    }

    private void launchSearch(String query, Label matchCountLabel) {
        this.searchResult.countOccurrences(query, this.searchProperties);
        matchCountLabel.setVisible(true);

        matchCountLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String s = "";
            int resultCount = this.searchResult.getResultCount();
            int currentIndex = this.searchResult.getCurrentSearchIndex();
            if (currentIndex >= 0) {
                s += "#" + (currentIndex + 1) + "/";
            }
            s += resultCount + " ";
            s += Configuration.langBundle.getString("matches_found");
            return s;
        }, this.searchResult.resultCountProperty(),this.searchResult.getCurrentSearchIndexProperty()));
    }

}

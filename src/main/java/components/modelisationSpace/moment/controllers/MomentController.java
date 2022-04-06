package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.appCommands.ConcreteCategoryCommandFactory;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.*;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListView;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;
import utils.popups.WarningPopup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MomentController extends ListViewController<Moment> implements Initializable {

    private Moment moment;
    private MomentCommandFactory cmdFactory;
    private ConcreteCategoryCommandFactory categoryCmdFactory;
    private MomentCommandFactory childCmdFactory;
    private ScrollPaneCommandFactory paneCmdFactory;
    private ModelisationSpaceHookNotifier modelisationSpaceHookNotifier;

    @FXML private AnchorPane categoryDropper;
    @FXML private BorderPane childrenMomentContainer;
    @FXML private BorderPane momentContainer;
    @FXML private Label momentName;
    @FXML private MenuButton menuButton;
    @FXML private HBox childrenBox;
    @FXML private VBox categoryContainer;
    @FXML private AnchorPane momentBoundingBox;
    @FXML private TextArea commentArea;
    @FXML HBox nameBox;
    @FXML private BorderPane momentBody;
    @FXML private ImageView collapseIcon;
    @FXML private HBox transitionBox;
    @FXML private BorderPane transitionPane;

    //Importants elements of a moment
    private JustificationController justificationController;
    private ListView<Moment, MomentController> momentsHBox;
    private ListView<ConcreteCategory, ConcreteCategoryController> categories;
    private boolean renamingMode = false;

    @FXML private GridPane grid;
    MomentSeparatorController separatorLeft, separatorRight, separatorBottom;

    private TextField renamingField;

    Node justificationArea;

    //menu-item with several values
    private MenuItem transitionButton;
    private MenuItem commentButton;

    private ChangeListener<Boolean> commentVisibleListener;
    private ChangeListener<Boolean> commentFocusListener;
    private ChangeListener<String> commentTextListener;
    private ChangeListener<Boolean> momentEmphasizeListener;
    private final ChangeListener<Boolean> collapsedListener = ((observable, oldValue, newValue) -> collapseOrNot());

    private static double TransitionalHeight = 950;


    public MomentController(Moment m, MomentCommandFactory cmdFactory, ScrollPaneCommandFactory paneCmdFactory) {
        this.moment = m;
        this.cmdFactory = cmdFactory;
        this.categoryCmdFactory = new ConcreteCategoryCommandFactory(cmdFactory.getHookNotifier(), moment);
        this.childCmdFactory = new MomentCommandFactory(cmdFactory.getHookNotifier(), moment);
        this.paneCmdFactory = paneCmdFactory;
        this.justificationController = new JustificationController(m.getJustification());

        separatorLeft = new MomentSeparatorController(true);
        separatorRight = new MomentSeparatorController(true);
        separatorBottom = new MomentSeparatorController(false);
    }

    public static Node createMoment(MomentController controller) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/modelisationSpace/moment/Moment.fxml"));
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
        moment.setController(this);
        grid.add(separatorBottom.getNode(), 1, 2);
        momentName.textProperty().bind(moment.nameProperty());
        commentArea.setVisible(moment.isCommentVisible());
        commentArea.managedProperty().bind(commentArea.visibleProperty());
        commentArea.setText(moment.getComment());

        bind();

        momentsHBox = new ListView<>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment,
                childrenBox);

        momentsHBox.setOnListUpdate(change -> separatorBottom.setActive(change.getList().size() == 0));

        justificationArea = JustificationController.createJustificationArea(justificationController);
        addJustifications();

        addCategories();


        //Listeners SETUP
        //bottom separator works only when there is no child yet !

        separatorBottom.setOnDragDoneDescripteme(descripteme -> childCmdFactory.addSiblingCommand(new Moment("Moment"), descripteme).execute());

        separatorBottom.setOnDragDoneCategory(category -> childCmdFactory.addSiblingCommand(new Moment("Moment"), category).execute());


        separatorBottom.setOnDragDoneShemaCategory(category -> childCmdFactory.addSiblingCommand(new Moment("Moment"), category, this.moment).execute());
        // category -> { cmdFactory.addSiblingCommand(new Moment("Moment"), category, 0).execute(); }
        separatorBottom.setOnDragMomentDone((moment, originParent) -> childCmdFactory.moveMomentCommand(moment, originParent).execute());
        separatorBottom.setOnDragTemplateMomentDone(templateMoment -> childCmdFactory.addSiblingCommand(templateMoment.createConcreteMoment()).execute());
        separatorBottom.setOnDragSchemaMomentType(schemaMomentType -> childCmdFactory.addSiblingCommand(new MomentType(schemaMomentType.getMomentType()), false).execute());

        //Menu Button
        if (commentArea.isVisible()) {
            commentButton = new MenuItem(Configuration.langBundle.getString("hide_comment"));
        } else {
            commentButton = new MenuItem(Configuration.langBundle.getString("show_comment"));
        }
        commentButton.setOnAction(actionEvent -> {
            commentArea.setVisible(!commentArea.isVisible());
            moment.setCommentVisible(commentArea.isVisible());
            updateTransHeight();
            if (commentArea.isVisible()) {
                commentButton.setText(Configuration.langBundle.getString("hide_comment"));
            } else {
                commentButton.setText(Configuration.langBundle.getString("show_comment"));
            }
        });
        menuButton.getItems().add(commentButton);

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> cmdFactory.deleteCommand(moment).execute());
        menuButton.getItems().add(deleteButton);

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> passInRenamingMode(true));
        menuButton.getItems().add(renameButton);

        addColorChange();

        if (moment.getTransitional()) {
            transitionButton = new MenuItem(Configuration.langBundle.getString("transitional_set_off"));
        } else {
            transitionButton = new MenuItem(Configuration.langBundle.getString("transitional_set_on"));
        }
        transitionButton.setOnAction(actionEvent -> {
            try { cmdFactory.transitionCommand(moment).execute();
            } catch (Error error) {
                WarningPopup.display(Configuration.langBundle.getString("transitional_warning"));
            }
        });
        menuButton.getItems().add(transitionButton);
        grid.add(transitionPane, 1, 1);
        displayTransitional();

        // Show/Hide moment body (comment, justifications, categories)
        collapseIcon.setOnMouseClicked(actionEvent -> {
            moment.setCollapsed(!moment.isCollapsed());
            collapseOrNot();
        });
        collapseOrNot();

        //DND
        setupDragAndDrop();

        //Rename moment
        momentName.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    passInRenamingMode(true);
                }
            }
        });
    }

    private void collapseOrNot() {
        if (!moment.isCollapsed()) {
            collapseIcon.setImage(new Image("/images/collapse_up.png"));
            momentBody.setCenter(commentArea);
            addJustifications();
            momentContainer.setBottom(categoryContainer);
        } else {
            collapseIcon.setImage(new Image("/images/collapse_down.png"));
            momentBody.setCenter(null);
            momentContainer.setCenter(null); // hide justifications

            // hide categories
            // when the moment is collapsed, there is only the moment names displayed
            VBox categoryNames = new VBox();
            categoryNames.setStyle("-fx-background-color: #ffffff;\n" +
                    "-fx-border-color: transparent;\n" +
                    "-fx-background-insets: 1px;\n" +
                    "-fx-background-radius: 3;\n" +
                    "-fx-border-radius:3;");
            moment.concreteCategoriesProperty().forEach((category) -> categoryNames.getChildren().add(new Label(category.getName())));
            momentContainer.setBottom(categoryNames);
        }
        updateTransHeight();
    }

    public void bind(){
        commentVisibleListener = (observableValue, oldValue, visible) -> moment.setCommentVisible(visible);

        //Add the comment; When the moment has no comment the textArea disappears
        commentFocusListener = (observableValue, oldValue, focused) -> {
            if(!focused){
                cmdFactory.addCommentCommand(moment, commentArea.getText()).execute();
                if (commentArea.getText() == null || commentArea.getText().isEmpty()){
                    commentArea.setVisible(false);
                }
            }
        };

        //in case of redo
        commentTextListener = (observableValue, oldValue, newValue) -> {
            if (!commentArea.isVisible() && !newValue.isEmpty()) {
                commentArea.setVisible(true);
            }
            commentArea.setText(newValue);
            //delete comments
            if(newValue == null || newValue.isEmpty()){
                commentArea.setVisible(false);
            }
        };

        // Emphasize
        momentEmphasizeListener = (observableValue, eventEventHandler, value) -> {
            if(value) {
                momentContainer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
            }
            else {
                momentContainer.setBorder(null);
            }
        };

        commentArea.visibleProperty().addListener(commentVisibleListener);
        commentArea.focusedProperty().addListener(commentFocusListener);
        moment.commentProperty().addListener(commentTextListener);
        moment.getEmphasizeProperty().addListener(momentEmphasizeListener);

        moment.collapsedProperty().addListener(collapsedListener);
    }


    public void unbind(){
        if (commentVisibleListener!=null)
            commentArea.visibleProperty().removeListener(commentVisibleListener);
        if (commentFocusListener!=null)
            commentArea.focusedProperty().removeListener(commentFocusListener);
        if (commentTextListener!=null)
            moment.commentProperty().removeListener(commentTextListener);
        if (momentEmphasizeListener!=null)
            moment.getEmphasizeProperty().removeListener(momentEmphasizeListener);

        moment.collapsedProperty().removeListener(collapsedListener);
    }

    private void addJustifications() {
        momentContainer.setCenter(justificationArea);
    }

    private void addCategories() {
        categories = new ListView<>(
                moment.concreteCategoriesProperty(),
                (cc -> new ConcreteCategoryController(cc, categoryCmdFactory, paneCmdFactory)),
                ConcreteCategoryController::create,
                categoryContainer
        );
    }

    public void passInRenamingMode(boolean YoN) {
        if(YoN != renamingMode) {
            if(YoN){
                renamingField = new AutoSuggestionsTextField(momentName.getText(), new SuggestionStrategyMoment());

                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal){   //unfocus
                        passInRenamingMode(false);
                    }
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER) {
                        passInRenamingMode(false);
                    }
                });
                this.nameBox.getChildren().clear();
                this.nameBox.getChildren().add(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            }
            else {
                if(renamingField.getLength() > 0 && !momentName.getText().equals(renamingField.getText())) {
                    HistoryManager.addCommand(new RenameMoment(moment, renamingField.getText()), true);
                }
                this.nameBox.getChildren().clear();
                this.nameBox.getChildren().add(momentName);
                renamingMode = false;
            }
        }
    }

    public void displayTransitional() {
        double depth;
        String color = getTransitionColor();
        transitionBox.setMaxHeight(0);
        transitionBox.setMaxWidth(20);
        transitionBox.setStyle("-fx-background-color: #" + color + ";\n");

        if (!moment.getTransitional()) {
            separatorBottom.setActive(moment.momentsProperty().size() == 0 && !moment.getTransitional());
            transitionButton.setText(Configuration.langBundle.getString("transitional_set_on"));
            categoryContainer.setStyle("-fx-background-color: #" + moment.getColor() + ";\n");
            momentContainer.setStyle("-fx-background-color: #" + moment.getColor() + ";\n");
        }
        else {
            depth = getTransitionDepth();
            separatorBottom.setActive(false);
            transitionBox.setMaxHeight(depth);
            transitionBox.setPrefHeight(depth);
            transitionButton.setText(Configuration.langBundle.getString("transitional_set_off"));
            categoryContainer.setStyle("-fx-background-color: #" + color + ";\n");
            momentContainer.setStyle("-fx-background-color: #" + color + ";\n");
        }
    }

    @Override
    public Moment getModel() {
        return moment;
    }

    @Override
    public void onMount() {
        Timeline viewFocus = new Timeline(new KeyFrame(Duration.seconds(0.1),
                event -> paneCmdFactory.scrollToNode(childrenMomentContainer).execute()));
        viewFocus.play();
    }

    @Override
    public void onUpdate(ListViewUpdate update) {
        updateBorders(update.newIndex, update.totalCount);
    }

    @Override
    public void onUnmount() {
        momentsHBox.onUnmount();
        categories.onUnmount();
        unbind();
    }

    private void updateBorders(int index, int siblingsCount) {
        separatorLeft.setOnDragDoneCategory(category -> cmdFactory.addSiblingCommand(new Moment("Moment"), category, 0).execute());
        separatorRight.setOnDragDoneCategory(category -> cmdFactory.addSiblingCommand(new Moment("Moment"), category, index+1).execute());

        separatorLeft.setOnDragDoneShemaCategory(category -> cmdFactory.addSiblingCommand(new Moment("Moment"), category, this.moment, 0).execute());
        separatorRight.setOnDragDoneShemaCategory(category -> cmdFactory.addSiblingCommand(new Moment("Moment"), category, this.moment, index+1).execute());

        separatorLeft.setOnDragSchemaMomentType(schemaMomentType -> cmdFactory.addSiblingCommand(new MomentType(schemaMomentType.getMomentType()), 0, false).execute());
        separatorRight.setOnDragSchemaMomentType(schemaMomentType -> cmdFactory.addSiblingCommand(new MomentType(schemaMomentType.getMomentType()), index+1, false).execute());

        if(index == 0) {
            //Hide an show the separators
            if(!grid.getChildren().contains(separatorLeft.getNode()))
                grid.add(separatorLeft.getNode(), 0, 0);
            if(!grid.getChildren().contains(separatorRight.getNode()))
                grid.add(separatorRight.getNode(), 2, 0);

            //set operation on descripteme DND over borders
            separatorLeft.setOnDragDoneDescripteme(descripteme -> cmdFactory.addSiblingCommand(new Moment("Moment"), descripteme, 0).execute());
            separatorRight.setOnDragDoneDescripteme(descripteme -> cmdFactory.addSiblingCommand(new Moment("Moment"), descripteme, index+1).execute());

            //set operation on moment DND over borders
            separatorLeft.setOnDragMomentDone((m, originParent) -> cmdFactory.moveMomentCommand(m, originParent, 0).execute());
            separatorRight.setOnDragMomentDone((m, originParent) -> cmdFactory.moveMomentCommand(m, originParent,index + 1).execute());

            separatorLeft.setOnDragTemplateMomentDone(templateMoment -> cmdFactory.addSiblingCommand(templateMoment.createConcreteMoment(), 0).execute());
            separatorRight.setOnDragTemplateMomentDone(templateMoment -> cmdFactory.addSiblingCommand(templateMoment.createConcreteMoment(), index+1).execute());

            //Make moment aligned, no need to understand that !
            Insets ins = childrenMomentContainer.getPadding();
            childrenMomentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), ins.getRight()));
        }
        else {
            //Hide an show the separators
            grid.getChildren().remove(separatorLeft.getNode());
            if(!grid.getChildren().contains(separatorRight.getNode()))
                grid.add(separatorRight.getNode(), 2, 0);

            //Do nothing with the left separator
            separatorLeft.setOnDragDoneDescripteme(descripteme -> {});
            separatorLeft.setOnDragMomentDone((m, factory) -> {});
            separatorLeft.setOnDragTemplateMomentDone(templateMoment -> {});
            if(index == siblingsCount - 1) {
                separatorRight.setOnDragDoneDescripteme(descripteme -> cmdFactory.addSiblingCommand(new Moment("Moment"), descripteme).execute());
                separatorRight.setOnDragMomentDone((m, originParent) -> cmdFactory.moveMomentCommand(m, originParent).execute());
                separatorRight.setOnDragTemplateMomentDone(templateMoment -> cmdFactory.addSiblingCommand(templateMoment.createConcreteMoment()).execute());
            }
            else {
                separatorRight.setOnDragDoneDescripteme(descripteme -> cmdFactory.addSiblingCommand(new Moment("Moment"), descripteme, index+1).execute());
                separatorRight.setOnDragMomentDone((m,originParent) -> cmdFactory.moveMomentCommand(m, originParent, index + 1).execute());
                separatorRight.setOnDragTemplateMomentDone(templateMoment -> cmdFactory.addSiblingCommand(templateMoment.createConcreteMoment(), index+1).execute());
            }

            //Make moment aligned, no need to understand that !
            Insets ins = childrenMomentContainer.getPadding();
            childrenMomentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), 0));
        }
        updateTransHeight();
    }

    private void setupDragAndDrop() {

        momentContainer.setOnDragOver(dragEvent -> {
            categoryDropper.setStyle("-fx-opacity: 1;");
            if(DragStore.getDraggable().isDraggable()) {
                //Descripteme
                if (
                    !dragEvent.isAccepted()
                    && DragStore.getDraggable().getDataFormat() == Descripteme.format
                    && justificationController.acceptDescripteme(DragStore.getDraggable())
                ) {
                    categoryDropper.setStyle("-fx-opacity: 0.5;");
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    dragEvent.consume();
                }
                //Simple Schema Category
                else if (
                    DragStore.getDraggable().getDataFormat() == SchemaCategory.format
                    && moment.indexOfSchemaCategory(DragStore.getDraggable()) == -1
                ) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    dragEvent.consume();
                }
                //Existing concrete category
                else if (
                    DragStore.getDraggable().getDataFormat() == ConcreteCategory.format
                    && !moment.hadThisCategory(DragStore.getDraggable())
                ) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    dragEvent.consume();
                }
                //Moment
                else if (
                        DragStore.getDraggable().getDataFormat() == Moment.format
                        && !moment.equals(DragStore.getDraggable())
                ) {
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                    dragEvent.consume();
                }
            }
        });

        momentContainer.setOnDragDropped(dragEvent -> {
            if(
                DragStore.getDraggable().getDataFormat() == Descripteme.format
                && dragEvent.isAccepted()
            ) {
                justificationController.addDescripteme(DragStore.getDraggable());
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
            else if(DragStore.getDraggable().getDataFormat() == SchemaCategory.format){
                categoryCmdFactory.addSchemaCategoryCommand(DragStore.getDraggable(), true).execute();
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
            else if(DragStore.getDraggable().getDataFormat() == ConcreteCategory.format) {
                categoryCmdFactory.addConcreteCategoryCommand(DragStore.getDraggable(), true).execute();
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
            else if(DragStore.getDraggable().getDataFormat() == Moment.format) {
                cmdFactory.mergeMomentCommand(moment, DragStore.getDraggable(),  true).execute();
                dragEvent.setDropCompleted(true);
                dragEvent.consume();
            }
        });

        momentContainer.setOnDragExited(dragEvent -> categoryDropper.setStyle("-fx-opacity: 1;"));

        momentContainer.setOnDragDetected(event -> {
            Dragboard db = momentContainer.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(moment.getDataFormat(), 0);
            DragStore.setDraggable(moment);
            DragStore.setDoubleObject(cmdFactory.getParentMoment()); //allows deleting the original one once drag is finished
            db.setContent(content);

            momentContainer.setOpacity(0.5);
            separatorLeft.setActive(false);
            separatorRight.setActive(false);
            separatorBottom.setActive(false);
        });

        momentContainer.setOnDragDone(event -> {
            event.consume();
            momentContainer.setOpacity(1);
            separatorLeft.setActive(true);
            separatorRight.setActive(true);
            separatorBottom.setActive(true);
        });
    }


    private double getTransitionDepth() {
        double height = getFullHeight();

        if (TransitionalHeight-height > 100) {
            return TransitionalHeight-height;
        }
        else {
            TransitionalHeight += 100;
            return getTransitionDepth();
        }
    }

    private String getTransitionColor() {
        int depth = moment.getDepth();
        if (depth == 1)
            return "888888";
        else if (depth == 2)
            return "9a9a9a";
        else if (depth == 3)
            return "acacac";
        else if (depth == 4)
            return "bebebe";
        else if (depth == 5)
            return "d0d0d0";
        else
            return "e0e0e0";
    }

    private double getFullHeight() {
        double parentHeight = 0;
        if (moment.getParent() != null) {
            try {
                Moment parentMoment = (Moment) moment.getParent();
                parentHeight = parentMoment.getController().getFullHeight()+16;
            } catch (ClassCastException error){
                //ignores when trying to get the rootMoment's height (it's not displayed)
            }
        }
        return parentHeight+momentContainer.getHeight();
    }

    private void updateTransHeight() {
        double height = getTransitionDepth();
        if (moment.getTransitional()) {
            transitionBox.setPrefHeight(height);
        }
    }

    public void updateColor() {
        categoryContainer.setStyle("-fx-background-color: #" + moment.getColor() + ";\n");
        momentContainer.setStyle("-fx-background-color: #" + moment.getColor() + ";\n");
    }

    private void addColorChange() {
        Menu changeColor = new Menu(Configuration.langBundle.getString("change_color"));

        MenuItem white = new MenuItem("    ");
        white.setStyle("-fx-background-color: #ffffff;\n");
        white.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "ffffff").execute());
        changeColor.getItems().add(white);

        MenuItem brown = new MenuItem("    ");
        brown.setStyle("-fx-background-color: #b97a57;\n");
        brown.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "b97a57").execute());
        changeColor.getItems().add(brown);

        MenuItem pink = new MenuItem("    ");
        pink.setStyle("-fx-background-color: #ffaec9;\n");
        pink.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "ffaec9").execute());
        changeColor.getItems().add(pink);

        MenuItem yellow = new MenuItem("    ");
        yellow.setStyle("-fx-background-color: #ffc90e;\n");
        yellow.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "ffc90e").execute());
        changeColor.getItems().add(yellow);

        MenuItem green = new MenuItem("    ");
        green.setStyle("-fx-background-color: #b5e61d;\n");
        green.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "b5e61d").execute());
        changeColor.getItems().add(green);

        MenuItem blue = new MenuItem("    ");
        blue.setStyle("-fx-background-color: #7092be;\n");
        blue.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "7092be").execute());
        changeColor.getItems().add(blue);

        MenuItem purple = new MenuItem("    ");
        purple.setStyle("-fx-background-color: #8671cd;\n");
        purple.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "8671cd").execute());
        changeColor.getItems().add(purple);

        MenuItem red = new MenuItem("    ");
        red.setStyle("-fx-background-color: #f15252;\n");
        red.setOnAction(actionEvent -> cmdFactory.colorCommand(moment, "f15252").execute());
        changeColor.getItems().add(red);

        menuButton.getItems().add(changeColor);
    }
}

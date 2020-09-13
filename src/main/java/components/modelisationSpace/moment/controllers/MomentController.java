package components.modelisationSpace.moment.controllers;

import application.configuration.Configuration;
import application.history.HistoryManager;
import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import models.Descripteme;
import components.modelisationSpace.appCommand.ScrollPaneCommandFactory;
import components.modelisationSpace.category.appCommands.ConcreteCategoryCommandFactory;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import models.ConcreteCategory;
import components.modelisationSpace.justification.controllers.JustificationController;
import components.modelisationSpace.moment.appCommands.MomentCommandFactory;
import models.Moment;
import models.SchemaCategory;
import components.modelisationSpace.moment.modelCommands.RenameMoment;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Duration;
import utils.autoSuggestion.AutoSuggestionsTextField;
import utils.autoSuggestion.strategies.SuggestionStrategyMoment;
import utils.dragAndDrop.DragStore;
import utils.modelControllers.ListView.ListView;
import utils.modelControllers.ListView.ListViewController;
import utils.modelControllers.ListView.ListViewUpdate;

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
    @FXML private BorderPane momentContainer;
    @FXML private BorderPane momentBody;
    @FXML private Label momentName;
    @FXML private Button btn;
    @FXML private MenuButton menuButton;
    @FXML private HBox childrenBox;
    @FXML private VBox categoryContainer;
    @FXML private AnchorPane momentBoundingBox;
    @FXML private TextArea commentArea;
    @FXML HBox nameBox;

    //Importants elements of a moment
    private JustificationController justificationController;
    private ListView<Moment, MomentController> momentsHBox;
    private ListView<ConcreteCategory, ConcreteCategoryController> categories;
    private boolean renamingMode = false;

    @FXML private GridPane grid;
    MomentSeparatorController separatorLeft, separatorRight, separatorBottom;

    private TextField renamingField;

    private ChangeListener<Boolean>commentVisibleListener;
    private ChangeListener<Boolean>commentFocusListener;
    private ChangeListener<String>commentTextListener;
    private ChangeListener<Boolean>momentEmphasizeListener;

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
        grid.add(separatorBottom.getNode(), 1, 1);
        momentName.textProperty().bind(moment.nameProperty());
        commentArea.setVisible(moment.isCommentVisible());
        commentArea.managedProperty().bind(commentArea.visibleProperty());
        commentArea.setText(moment.getComment());

        bind();

        //Setup de la zone de DND des descriptemes
        momentBody.setCenter(JustificationController.createJustificationArea(justificationController));
        //Setup de la HBox pour les enfants
        momentsHBox = new ListView<>(
                moment.momentsProperty(),
                (m -> new MomentController(m, childCmdFactory, paneCmdFactory)),
                MomentController::createMoment,
                childrenBox);
        momentsHBox.setOnListUpdate(change -> separatorBottom.setActive(change.getList().size() == 0));

        categories = new ListView<>(
                moment.concreteCategoriesProperty(),
                (cc -> new ConcreteCategoryController(cc, categoryCmdFactory, paneCmdFactory)),
                ConcreteCategoryController::create,
                categoryContainer
        );


        //Listeners SETUP
        //bottom separator works only when there is no child yet !
        separatorBottom.setOnDragDoneDescripteme(descripteme -> childCmdFactory.addSiblingCommand(new Moment("Moment"), descripteme).execute());
        separatorBottom.setOnDragDoneCategory(category -> childCmdFactory.addSiblingCommand(new Moment("Moment"), category).execute());
        separatorBottom.setOnDragDoneShemaCategory(category -> childCmdFactory.addSiblingCommand(new Moment("Moment"), category, this.moment).execute());
        // category -> { cmdFactory.addSiblingCommand(new Moment("Moment"), category, 0).execute(); }
        separatorBottom.setOnDragMomentDone((moment, originParent) -> childCmdFactory.moveMomentCommand(moment, originParent).execute());
        separatorBottom.setOnDragTemplateMomentDone(templateMoment -> childCmdFactory.addSiblingCommand(templateMoment.createConcreteMoment()).execute());
        separatorBottom.setActive(moment.momentsProperty().size() == 0);

        //Menu Button
        MenuItem commentButton = new MenuItem(Configuration.langBundle.getString("show_hide_comment"));
        commentButton.setOnAction(actionEvent -> {
            commentArea.setVisible(!commentArea.isVisible());
            moment.setCommentVisible(commentArea.isVisible());
        });
        menuButton.getItems().add(commentButton);

        MenuItem deleteButton = new MenuItem(Configuration.langBundle.getString("delete"));
        deleteButton.setOnAction(actionEvent -> cmdFactory.deleteCommand(moment).execute());
        menuButton.getItems().add(deleteButton);

        MenuItem renameButton = new MenuItem(Configuration.langBundle.getString("rename"));
        renameButton.setOnAction(actionEvent -> cmdFactory.renameCommand(moment).execute());
        menuButton.getItems().add(renameButton);


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
    public void bind(){
        commentVisibleListener = (observableValue, oldValue, visible) -> {
            moment.setCommentVisible(visible);
        };

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
                momentBody.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
            }
            else {
                momentBody.setBorder(null);
            }
        };

        commentArea.visibleProperty().addListener(commentVisibleListener);
        commentArea.focusedProperty().addListener(commentFocusListener);
        moment.commentProperty().addListener(commentTextListener);
        moment.getEmphasizeProperty().addListener(momentEmphasizeListener);

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

    }

    public void passInRenamingMode(boolean YoN) {
        if(YoN != renamingMode) {
            if(YoN){
                renamingField = new AutoSuggestionsTextField(momentName.getText(), new SuggestionStrategyMoment());

                renamingField.setAlignment(Pos.CENTER);
                renamingField.end();
                renamingField.selectAll();

                renamingField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal)
                        passInRenamingMode(false);
                });

                renamingField.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER) {
                        if(renamingField.getLength() > 0)
                        HistoryManager.addCommand(new RenameMoment(moment, renamingField.getText()), true);
                        passInRenamingMode(false);
                    }
                });
                this.nameBox.getChildren().clear();
                this.nameBox.getChildren().add(renamingField);
                renamingField.requestFocus();
                renamingMode = true;
            }
            else {
                if(renamingField.getLength() > 0)
                    HistoryManager.addCommand(new RenameMoment(moment, renamingField.getText()), true);
                this.nameBox.getChildren().clear();
                this.nameBox.getChildren().add(momentName);
                renamingMode = false;
            }
        }
    }
    @Override
    public Moment getModel() {
        return moment;
    }

    @Override
    public void onMount() {
        Timeline viewFocus = new Timeline(new KeyFrame(Duration.seconds(0.1),
            (EventHandler<ActionEvent>) event -> {
                paneCmdFactory.scrollToNode(momentContainer).execute();
        }));
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
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), ins.getRight()));
        }
        else {
            //Hide an show the separators
            if(grid.getChildren().contains(separatorLeft.getNode()))
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
            Insets ins = momentContainer.getPadding();
            momentContainer.setPadding(new Insets(ins.getTop(), ins.getRight(), ins.getBottom(), 0));
        }
    }

    private void setupDragAndDrop() {

        momentBody.setOnDragOver(dragEvent -> {
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
            }
        });

        momentBody.setOnDragDropped(dragEvent -> {
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
        });

        momentBody.setOnDragExited(dragEvent -> categoryDropper.setStyle("-fx-opacity: 1;"));

        momentBody.setOnDragDetected(event -> {
            System.out.println(" moment drag detected");
            Dragboard db = momentBody.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(moment.getDataFormat(), 0);
            DragStore.setDraggable(moment);
            DragStore.setDoubleObject(cmdFactory.getParentMoment()); //allows deleting the original one once drag is finished
            db.setContent(content);

            momentBody.setOpacity(0.5);
            separatorLeft.setActive(false);
            separatorRight.setActive(false);
            separatorBottom.setActive(false);
        });

        momentBody.setOnDragDone(event -> {
            event.consume();
            momentBody.setOpacity(1);
            separatorLeft.setActive(true);
            separatorRight.setActive(true);
            separatorBottom.setActive(true);
        });
    }

}

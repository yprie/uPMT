package components.interviewPanel.Controllers;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.RemoveAnnotationCommand;
import components.interviewPanel.utils.LetterMap;
import components.interviewPanel.utils.TextStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import models.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import utils.GlobalVariables;

import java.time.Duration;
import java.util.ArrayList;

public class RichTextAreaController {
    private InlineCssTextArea area;
    private ContextMenu menu;

    private InterviewText interviewText;
    private VirtualizedScrollPane<InlineCssTextArea> vsPane;
    private LetterMap letterMap;
    private SimpleObjectProperty<IndexRange> userSelection;
    private ArrayList<Descripteme> emphasizedDescriptemes; // used temporary when over a descripteme
    private ArrayList<Moment> emphasizedMoments = new ArrayList<>(); // used temporary when over a descripteme
    private Color toolColorSelected; // the selected tool, if null -> default tool is descripteme

    private ListChangeListener<Annotation> onAnnotationsChangeListener = change -> {
        while (change.next()) {
            for (Annotation removed : change.getRemoved()) {
                letterMap.removeAnnotation(removed);
                applyStyle(removed);
            }
            for (Annotation added : change.getAddedSubList()) {
                letterMap.becomeAnnotation(added, added.getColor());
                applyStyle(added);
            }
        }
    };
    private ListChangeListener<Descripteme> onDescriptemeChangeListener = change -> {
        while (change.next()) {
            for (Descripteme removed : change.getRemoved()) {
                letterMap.removeDescripteme(removed);
                applyStyle(removed);
            }
            for (Descripteme added : change.getAddedSubList()) {
                letterMap.becomeDescripteme(added);
                applyStyle(added);
            }
        }
    };

    public RichTextAreaController(InterviewText interviewText) {
        this.interviewText = interviewText;
        this.letterMap = new LetterMap();
        userSelection = new SimpleObjectProperty<>();
        area = new InlineCssTextArea();
        area.setWrapText(true);
        area.setEditable(false);
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.appendText(interviewText.getText());
        area.setShowCaret(Caret.CaretVisibility.ON);

        setUpClick();
        setUpPopUp();
        setUpMenu();

        this.interviewText.getAnnotationsProperty().addListener(
                new WeakListChangeListener<>(onAnnotationsChangeListener));
        this.interviewText.getDescriptemesProperty().addListener(
                new WeakListChangeListener<>(onDescriptemeChangeListener));

        GlobalVariables.getGlobalVariables()
                .getDescriptemeChangedProperty()
                .addListener(newValue -> { this.updateDescripteme(); });
    }

    private void setUpClick() {
        area.setOnMouseReleased(event -> {
            userSelection.set(new IndexRange(
                    area.getSelection().getStart(),
                    area.getSelection().getEnd()
            ));

            if (toolColorSelected != null) {
                annotate(toolColorSelected);
            }
        });

        area.setOnMousePressed(event -> {
            Annotation previousSelected = letterMap.getSelectedAnnotation();
            if (previousSelected != null) {
                letterMap.deSelectAnnotation();
                applyStyle(previousSelected);
            }

            Annotation annotation = interviewText.getFirstAnnotationByIndex(area.getCaretPosition());
            if (annotation != null) {
                letterMap.selectAnnotation(annotation);
                applyStyle(annotation);
            }
        });
    }

    private void setUpPopUp() {
        Popup popup = new Popup();
        Label popupMsg = new Label();
        popupMsg.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5;");
        popup.getContent().add(popupMsg);

        area.setMouseOverTextDelay(Duration.ofMillis(500));
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
            Point2D pos = event.getScreenPosition();

            // emphasize descripteme in modeling space
            emphasizedDescriptemes = interviewText.getDescriptemesByIndex(event.getCharacterIndex());
            if (emphasizedDescriptemes != null) {
                emphasizedMoments.clear();
                for (Descripteme descripteme : emphasizedDescriptemes) {
                    descripteme.getEmphasizeProperty().set(true);
                    emphasizedMoments.addAll(GlobalVariables.getGlobalVariables().getMomentsByDescripteme(descripteme));
                }
                for(Moment moment : emphasizedMoments) {
                    moment.getEmphasizeProperty().set(true);
                }
            }
        });
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, event -> {
            popup.hide();

            // de-emphasize descripteme in modeling space
            if (emphasizedDescriptemes != null) {
                for (Descripteme descripteme : emphasizedDescriptemes) {
                    descripteme.getEmphasizeProperty().set(false);
                }
                emphasizedDescriptemes = null;
            }
            if (!emphasizedMoments.isEmpty()) {
                for(Moment moment : emphasizedMoments) {
                    moment.getEmphasizeProperty().set(false);
                }
                emphasizedMoments.clear();
            }
        });
    }

    private  void setUpMenu() {
        menu = new ContextMenu();

        MenuItem deleteAnnotationMenuItem = new MenuItem("Delete annotation");
        deleteAnnotationMenuItem.setOnAction(event -> {
            deleteAnnotation(area.getCaretPosition());
        });
        MenuItem item1 = new MenuItem("Yellow");
        item1.setOnAction(e -> {
            if (!area.getSelectedText().isEmpty()) {
                annotate(Color.YELLOW,
                        userSelection.get().getStart(),
                        userSelection.get().getEnd());
            }
        });
        MenuItem item2 = new MenuItem("Red");
        item2.setOnAction(e -> {
            if (!area.getSelectedText().isEmpty()) {
                annotate(Color.RED,
                        userSelection.get().getStart(),
                        userSelection.get().getEnd());
            }
        });
        MenuItem item3 = new MenuItem("Blue");
        item3.setOnAction(e -> {
            if (!area.getSelectedText().isEmpty()) {
                annotate(Color.BLUE,
                        userSelection.get().getStart(),
                        userSelection.get().getEnd());
            }
        });
        menu.getItems().addAll(deleteAnnotationMenuItem, item1, item2, item3);
        area.setContextMenu(menu);
    }

    private void updateDescripteme() {
        Descripteme changedDescripteme = GlobalVariables.getGlobalVariables()
                .getDescriptemeChangedProperty().getValue();
        if (!GlobalVariables.getGlobalVariables().getAllDescripteme().contains(changedDescripteme)) {
            // the change is a deletion of the descripteme
            interviewText.getDescriptemesProperty().remove(changedDescripteme);
        }
    }

    private void applyStyle(Fragment fragment) {
        for (int i = fragment.getStartIndex() ; i < fragment.getEndIndex() ; i++) {
            TextStyle style = letterMap.getStyleByIndex(i);
            String css = "";
            if (style.getIsAnnotation()) {
                css += "-rtfx-background-color: " + style.getCSSColor() + ";";
            }
            if (style.getIsDescripteme()) {
                css += "-rtfx-underline-color: black; " + "-rtfx-underline-width: 1.5;";
            }
            area.setStyle(i, i+1, css);
        }
    }

    private void annotate(Color color, Integer start, Integer end) {
        Annotation annotation = new Annotation(interviewText,
                start,
                end,
                color);
        new AddAnnotationCommand(interviewText, annotation).execute();
    }

    private void deleteAnnotation(Integer index) {
        Annotation annotation = interviewText.getFirstAnnotationByIndex(index);
        new RemoveAnnotationCommand(interviewText, annotation).execute();
        // there is a listener that apply the style
    }


    public void annotate(Color color) {
        IndexRange selection = area.getSelection();
        if (selection.getStart() != selection.getEnd()) {
            annotate(color, selection.getStart(), selection.getEnd());
            area.deselect();
        }
    }

    public void deleteAnnotation() {
        Annotation annotation = interviewText.getFirstAnnotationByIndex(area.getCaretPosition());
        new RemoveAnnotationCommand(interviewText, annotation).execute();
        // there is a listener that apply the style
    }

    public String getSelectedText() {
        return area.getSelectedText();
    }

    public IndexRange getSelection() {
        return area.getSelection();
    }

    public VirtualizedScrollPane<InlineCssTextArea> getNode() {
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane(area);
        this.vsPane = vsPane;
        return vsPane;
    }

    public SimpleObjectProperty<IndexRange> getUserSelection() {
        return userSelection;
    }

    public void deselect() {
        area.deselect();
    }

    public void addDescripteme(Descripteme descripteme) {
        interviewText.addDescripteme(descripteme);
    }

    public void setToolColorSelected(Color color) {
        toolColorSelected = color;
    }

    public Color getToolColorSelected() {
        return toolColorSelected;
    }
}

package components.interviewPanel.Controllers;

import components.interviewPanel.ContextMenus.ContextMenuFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Popup;
import models.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utils.GlobalVariables.getGlobalVariables;

public class RichTextAreaController {
    private final InlineCssTextArea area;
    private Annotation selectedAnnotation;
    private final InterviewText interviewText;
    private final SimpleObjectProperty<IndexRange> userSelection;
    private ArrayList<Descripteme> emphasizedDescriptemes = new ArrayList<>(); // used temporary when over a descripteme
    private final ArrayList<Moment> emphasizedMoments = new ArrayList<>(); // used temporary when over a descripteme
    private ContextMenuFactory contextMenuFactory;
    private final List<AnnotationColor> annotationColorList;

    public RichTextAreaController(InterviewText interviewText, List<AnnotationColor> annotationColorList) {
        this.interviewText = interviewText;
        this.annotationColorList = annotationColorList;
        userSelection = new SimpleObjectProperty<>();
        area = new InlineCssTextArea();
        area.setWrapText(true);
        area.setEditable(false);
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.appendText(interviewText.getText());
        area.setShowCaret(Caret.CaretVisibility.ON);

        setUpClick();
        setUpPopUp();

        // Two listeners that update the view (highlight and underline)
        this.interviewText.getAnnotationsProperty().addListener((ListChangeListener.Change<? extends Annotation> c) -> {
            while (c.next()) {
                for (Annotation removed : c.getRemoved()) {
                    applyStyle(removed.getStartIndex(), removed.getEndIndex());
                    area.deselect();
                }
                for (Annotation added : c.getAddedSubList()) {
                    applyStyle(added.getStartIndex(), added.getEndIndex());
                    area.deselect();
                }
            }
        });

        this.interviewText.getDescriptemesProperty().addListener((ListChangeListener.Change<? extends Descripteme> c) -> {
            while (c.next()) {
                for (Descripteme removed : c.getRemoved()) {
                    applyStyle(removed.getStartIndex(), removed.getEndIndex());
                    area.deselect();
                }
                for (Descripteme added : c.getAddedSubList()) {
                    applyStyle(added.getStartIndex(), added.getEndIndex());
                    area.deselect();
                }
            }
        });

        // Watch for new descriptemes
        getGlobalVariables()
                .getDescriptemeChangedProperty()
                .addListener(newValue -> this.updateDescripteme());

        // Initialize view annotation
        interviewText.getAnnotationsProperty().forEach(annotation -> applyStyle(annotation.getStartIndex(), annotation.getEndIndex()));


        area.caretPositionProperty().addListener(event -> {
            System.out.println("caret position changed: " + area.caretPositionProperty().getValue());
        });
    }

    public void setContextMenuFactory(ContextMenuFactory contextMenuFactory) {
        this.contextMenuFactory = contextMenuFactory;
    }

    private void setUpClick() {
        area.setOnMousePressed(event -> {
            area.getContextMenu().hide();

            if (selectedAnnotation != null) {
                Platform.runLater(() -> {
                    if (interviewText.getAnnotationsProperty().get().contains(selectedAnnotation)) {
                        selectedAnnotation.setSelected(false);
                        applyStyle(selectedAnnotation.getStartIndex(), selectedAnnotation.getEndIndex());
                        selectedAnnotation = null;
                    }
                });
            }

            Annotation annotation = interviewText.getAnnotationByIndex(area.getCaretPosition());
            if (annotation != null) {
                Platform.runLater(() -> {
                    annotation.setSelected(true);
                    applyStyle(annotation.getStartIndex(), annotation.getEndIndex());
                    selectedAnnotation = annotation;
                });
            }
        });

        area.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY || event.isControlDown()) {
                System.out.println("mouse right click "
                        + area.hit(event.getX(), event.getY()).getInsertionIndex()
                        + " " + area.hit(event.getX(), event.getY()).getCharacterIndex());
                area.getCaretSelectionBind().moveTo(area.hit(event.getX(), event.getY()).getInsertionIndex());
                updateContextMenu();
                area.getContextMenu().show(area, event.getScreenX(), event.getScreenY());
            }
        });

        // Remove this and bind area.selectionProperty()
        area.setOnMouseReleased(event -> userSelection.set(new IndexRange(
                area.getSelection().getStart(),
                area.getSelection().getEnd()
        )));
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

            // TODO: create a app command, because context menu could use it
            // emphasize descripteme in modeling space
            emphasizedDescriptemes = interviewText.getDescriptemesByIndex(event.getCharacterIndex());
            if (!emphasizedDescriptemes.isEmpty()) {
                String message = emphasizedDescriptemes.size() + " descripteme(s)";
                popup.show(area, pos.getX(), pos.getY() + 10);

                emphasizedMoments.clear();
                for (Descripteme descripteme : emphasizedDescriptemes) {
                    descripteme.getEmphasizeProperty().set(true);
                    emphasizedMoments.addAll(getGlobalVariables().getMomentsByDescripteme(descripteme));
                }
                for(Moment moment : emphasizedMoments) {
                    moment.getEmphasizeProperty().set(true);
                }
                message += " dans " + emphasizedMoments.size() + " moment(s)";

                popupMsg.setText(message);
                popup.show(area, pos.getX(), pos.getY() + 10);
            }
        });
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, event -> {
            popup.hide();

            // de-emphasize descripteme in modeling space
            if (!emphasizedDescriptemes.isEmpty()) {
                for (Descripteme descripteme : emphasizedDescriptemes) {
                    descripteme.getEmphasizeProperty().set(false);
                }
                emphasizedDescriptemes.clear();
            }
            if (!emphasizedMoments.isEmpty()) {
                for(Moment moment : emphasizedMoments) {
                    moment.getEmphasizeProperty().set(false);
                }
                emphasizedMoments.clear();
            }
        });
    }

    private void updateDescripteme() {
        Descripteme changedDescripteme = getGlobalVariables()
                .getDescriptemeChangedProperty().getValue();
        if (!getGlobalVariables().getAllDescripteme().contains(changedDescripteme)) {
            // the change is a deletion of the descripteme
            interviewText.getDescriptemesProperty().remove(changedDescripteme);
        }
    }

    private void applyStyle(int start, int end) {
        for (int i = start ; i < end ; i++) {
            String css = "";
            Annotation annotation = interviewText.getAnnotationByIndex(i);
            if (annotation != null) {
                css += "-rtfx-background-color: " + annotation.getColor().toString().replace("0x", "#") + ";";
                if (annotation.isSelected()) {
                    css += "-rtfx-border-stroke-color: black; " +
                            "-rtfx-border-stroke-width: 1;";
                }
            }
            ArrayList<Descripteme> descriptemes = interviewText.getDescriptemesByIndex(i);
            if (descriptemes.size() == 1) {
                css += "-rtfx-underline-color: black; " + "-rtfx-underline-width: 1;";
            }
            else if (descriptemes.size() > 1) {
                css += "-rtfx-underline-color: black; " + "-rtfx-underline-width: 2;";
            }
            area.setStyle(i, i+1, css);
        }
    }

    public IndexRange getSelection() {
        return area.getSelection();
    }

    public VirtualizedScrollPane<InlineCssTextArea> getNode() {
        return new VirtualizedScrollPane(area);
    }

    public SimpleObjectProperty<IndexRange> getUserSelection() {
        return userSelection;
    }

    public void addDescripteme(Descripteme descripteme) {
        interviewText.addDescripteme(descripteme);
        descripteme.startIndexProperty().addListener((observable, oldValue, newValue) -> {
            // create a temporary descripteme with the shape avec the previous descripteme...
            Descripteme temp = new Descripteme(interviewText, oldValue.intValue(), descripteme.getEndIndex());
            // ... in order to be able to delete the underline
            //letterMap.removeDescripteme(temp);
            applyStyle(temp.getStartIndex(), temp.getEndIndex());
            //letterMap.becomeDescripteme(descripteme);
            applyStyle(descripteme.getStartIndex(), descripteme.getEndIndex());
        });
        descripteme.endIndexProperty().addListener((observable, oldValue, newValue) -> {
            // create a temporary descripteme with the shape avec the previous descripteme...
            Descripteme temp = new Descripteme(interviewText, descripteme.getStartIndex(), oldValue.intValue());
            // ... in order to be able to delete the underline
            //letterMap.removeDescripteme(temp);
            applyStyle(temp.getStartIndex(), temp.getEndIndex());
            //letterMap.becomeDescripteme(descripteme);
            applyStyle(descripteme.getStartIndex(), descripteme.getEndIndex());
        });
    }

    public void select(IndexRange selection) {
        area.selectRange(selection.getStart(), selection.getEnd());
    }

    public void updateContextMenu() {
        area.setContextMenu(null);
        Annotation annotation = interviewText.getAnnotationByIndex(area.getCaretPosition());
        ArrayList<Descripteme> descriptemes = interviewText.getDescriptemesByIndex(area.getCaretPosition());
        if (annotation != null && !descriptemes.isEmpty()) {
            System.out.println("we have selected an annotation and a descripteme");
            area.setContextMenu(contextMenuFactory.getContextMenuDescriptemeAndAnnotation(descriptemes, annotation));
        }
        else if (annotation != null) {
            System.out.println("we are in an annotation");
            area.setContextMenu(contextMenuFactory.getContextMenuAnnotation(annotation));
        }
        else if (!descriptemes.isEmpty()) {
            System.out.println("we are in descriptemes");
            area.setContextMenu(contextMenuFactory.getContextMenuDescripteme(descriptemes));
        }
        else if (!area.getSelectedText().isEmpty()) {
            System.out.println("we have just a text selection");
            area.setContextMenu(contextMenuFactory.getContextMenuSelection(area.getSelection()));
        }
    }
}

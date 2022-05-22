package components.interviewPanel.Controllers;

import components.interviewPanel.ContextMenus.ContextMenuFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import models.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static utils.GlobalVariables.getGlobalVariables;

public class RichTextAreaController {
    private final InlineCssTextArea area;
    private Annotation selectedAnnotation;
    private final InterviewText interviewText;
    private final SimpleObjectProperty<IndexRange> userSelection;
    private ArrayList<Descripteme> emphasizedDescriptemes = new ArrayList<>(); // used temporary when over a descripteme
    private final HashSet<Moment> emphasizedMoments = new HashSet<>(); // used temporary when over a descripteme
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

        bind();

        // Watch for new descriptemes
        getGlobalVariables()
                .getDescriptemeChangedProperty()
                .addListener(newValue -> this.updateDescripteme());

        // Initialize view annotation
        //interviewText.getAnnotationsProperty().forEach(annotation -> applyStyle(annotation.getStartIndex(), annotation.getEndIndex()));
        applyStyleInitialize();
    }

    public void setContextMenuFactory(ContextMenuFactory contextMenuFactory) {
        this.contextMenuFactory = contextMenuFactory;
    }

    private void setUpClick() {
        area.setOnMousePressed(event -> {
            area.getCaretSelectionBind().moveTo(area.hit(event.getX(), event.getY()).getInsertionIndex());
            updateContextMenu();

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

        // Remove this and bind area.selectionProperty()
        area.setOnMouseReleased(event -> userSelection.set(new IndexRange(
                area.getSelection().getStart(),
                area.getSelection().getEnd()
        )));
    }

    public void bind() {
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
                    bindDescripteme(removed, false);
                    applyStyle(removed.getStartIndex(), removed.getEndIndex());
                    area.deselect();
                }
                for (Descripteme added : c.getAddedSubList()) {
                    bindDescripteme(added, true);
                    applyStyle(added.getStartIndex(), added.getEndIndex());
                    area.deselect();
                }
            }
        });
    }

    private void setUpPopUp() {
        Popup popup = new Popup();
        Label popupMsg = new Label();
        popupMsg.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5;" +
                        "-fx-font-family: serif;");
        popup.getContent().add(popupMsg);

        area.setMouseOverTextDelay(Duration.ofMillis(500));
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
            Point2D pos = event.getScreenPosition();

            // TODO: create a app command, because context menu could use it
            // emphasize descripteme in modeling space
            emphasizedDescriptemes = interviewText.getDescriptemesByIndex(event.getCharacterIndex());
            if (!emphasizedDescriptemes.isEmpty()) {
                String message = emphasizedDescriptemes.size() + " descripteme(s): ";
                popup.show(area, pos.getX(), pos.getY() + 10);

                emphasizedMoments.clear();
                for (Descripteme descripteme : emphasizedDescriptemes) {
                    descripteme.getEmphasizeProperty().set(true);
                    emphasizedMoments.addAll(getGlobalVariables().getMomentsByDescripteme(descripteme));
                }
                for(Moment moment : emphasizedMoments) {
                    moment.getEmphasizeProperty().set(true);
                }
                message += + emphasizedMoments.size() + " moment(s)";

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

    private void applyStyleInitialize() {
        interviewText.getAnnotationsProperty().forEach(annotation -> {
            area.setStyle(
                    annotation.getStartIndex(),
                    annotation.getEndIndex(),
                    "-rtfx-background-color: " + annotation.getColor().toString().replace("0x", "#") + ";"
            );
        });
        /*HashMap<Integer, String> styles = new HashMap<>();
        List<Integer> changes = new ArrayList<>();
        interviewText.getDescriptemesProperty().forEach(descripteme -> {
            changes.add(descripteme.getStartIndex());
            changes.add(descripteme.getEndIndex());
        });*/
    }

    private void applyStyle(int start, int end) {
        for (int i = start ; i < end ; i++) {
            area.setStyle(i, i+1, getCSS(i));
        }
    }

    private String getCSS(int i) {
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
        if (descriptemes.size() >= 1) {
            int size = 1;
            if (descriptemes.size() >= 2) {
                size = 2;
            }
            css += "-rtfx-underline-color: black; " + "-rtfx-underline-width: " + size + ";";
            boolean isRevealed = false;
            for (Descripteme descripteme: descriptemes) {
                if (descripteme.getRevealedProperty().get()) {
                    isRevealed = true;
                    break;
                }
            }
            if (isRevealed) {
                css += "-rtfx-border-stroke-dash-array: 5;-rtfx-border-stroke-color: black;-rtfx-border-stroke-width: 1;";
            }
        }
        return css;
    }

    private void bindDescripteme(Descripteme descripteme, boolean bind) {
        ChangeListener listenerStartIndex = (ChangeListener<Number>) (observable, oldValue, newValue) -> {
            // create a temporary descripteme with the shape of the previous descripteme...
            Descripteme temp = new Descripteme(interviewText, oldValue.intValue(), descripteme.getEndIndex());
            // ... in order to be able to delete the underline
            applyStyle(temp.getStartIndex(), temp.getEndIndex());
            applyStyle(descripteme.getStartIndex(), descripteme.getEndIndex());
        };
        ChangeListener listenerEndIndex = (ChangeListener<Number>) (observable, oldValue, newValue) -> {
            // create a temporary descripteme with the shape avec the previous descripteme...
            Descripteme temp = new Descripteme(interviewText, descripteme.getStartIndex(), oldValue.intValue());
            // ... in order to be able to delete the underline
            applyStyle(temp.getStartIndex(), temp.getEndIndex());
            applyStyle(descripteme.getStartIndex(), descripteme.getEndIndex());
        };
        ChangeListener listenerRevealed = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            // Surround the descripteme in the interview
            applyStyle(descripteme.getStartIndex(), descripteme.getEndIndex());
        };

        ChangeListener listenerScrollToTrigger = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (descripteme.getTriggerScrollReveal().getValue()) {
                area.moveTo(0);
                area.requestFollowCaret();
                Platform.runLater(() -> {
                    area.moveTo(descripteme.getStartIndex());
                    area.requestFollowCaret();
                    Platform.runLater(() -> {
                        area.scrollYBy(area.getHeight() / 2);
                    });
                });
            }
            descripteme.setTriggerScrollReveal(false);
        };


        if (bind) {
            descripteme.startIndexProperty().addListener(listenerStartIndex);
            descripteme.endIndexProperty().addListener(listenerEndIndex);
            descripteme.getRevealedProperty().addListener(listenerRevealed);
            descripteme.getTriggerScrollReveal().addListener(listenerScrollToTrigger);
        }
        else {
            descripteme.getRevealedProperty().removeListener(listenerStartIndex);
            descripteme.getRevealedProperty().removeListener(listenerEndIndex);
            descripteme.getRevealedProperty().removeListener(listenerRevealed);
            descripteme.getTriggerScrollReveal().removeListener(listenerScrollToTrigger);
        }
    }

    public IndexRange getSelection() {
        return area.getSelection();
    }

    public VirtualizedScrollPane<InlineCssTextArea> getNode() {
        return new VirtualizedScrollPane<>(area);
    }

    public SimpleObjectProperty<IndexRange> getUserSelection() {
        return userSelection;
    }

    public void select(IndexRange selection) {
        area.selectRange(selection.getStart(), selection.getEnd());
    }

    public void updateContextMenu() {
        area.setContextMenu(null);
        Annotation annotation = interviewText.getAnnotationByIndex(area.getCaretPosition());
        ArrayList<Descripteme> descriptemes = interviewText.getDescriptemesByIndex(area.getCaretPosition());
        if (annotation != null && !descriptemes.isEmpty()) {
            area.setContextMenu(contextMenuFactory.getContextMenuDescriptemeAndAnnotation(
                    area.getSelectedText(),
                    descriptemes,
                    annotation)
            );
        }
        else if (annotation != null) {
            area.setContextMenu(contextMenuFactory.getContextMenuAnnotation(
                    area.getSelectedText(),
                    annotation)
            );
        }
        else if (!descriptemes.isEmpty()) {
            area.setContextMenu(contextMenuFactory.getContextMenuDescripteme(
                    area.getSelectedText(),
                    descriptemes)
            );
        }
        else if (!area.getSelectedText().isEmpty()) {
            area.setContextMenu(contextMenuFactory.getContextMenuSelection(
                    area.getSelectedText(),
                    area.getSelection())
            );
        }
    }
}

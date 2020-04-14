package components.interviewPanel.Controllers;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.RemoveAnnotationCommand;
import components.interviewPanel.utils.LetterMap;
import components.interviewPanel.utils.TextStyle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import models.Annotation;
import models.Descripteme;
import models.Fragment;
import models.InterviewText;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import utils.GlobalVariables;

import java.time.Duration;

public class RichTextAreaController {
    private InlineCssTextArea area;
    private ContextMenu menu;
    private MenuItem deleteAnnotationMenuItem;

    private InterviewText interviewText;
    private VirtualizedScrollPane<InlineCssTextArea> vsPane;
    private LetterMap letterMap;
    private SimpleObjectProperty<IndexRange> userSelection;
    private Descripteme emphazedDescripteme;

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

        setUpMenu();
        setUpPopUp();
        area.setOnMouseReleased(event -> {
            userSelection.set(new IndexRange(
                    area.getSelection().getStart(),
                    area.getSelection().getEnd()
            ));
        });

        this.interviewText.getAnnotationsProperty().addListener(
                new WeakListChangeListener<>(onAnnotationsChangeListener));
        this.interviewText.getDescriptemesProperty().addListener(
                new WeakListChangeListener<>(onDescriptemeChangeListener));

        GlobalVariables.getGlobalVariables()
                .getDescriptemeChangedProperty()
                .addListener(newValue -> { this.updateDescripteme(); });
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

            Annotation annotation = interviewText.getFirstAnnotationByIndex(event.getCharacterIndex());
            if (annotation != null) {
                popupMsg.setText(annotation.toString());
                popup.show(area, pos.getX(), pos.getY() + 5);
            }

            // emphasize descripteme in modeling space
            Descripteme descripteme = interviewText.getFirstDescriptemeByIndex(event.getCharacterIndex());
            if (descripteme != null) {
                descripteme.getEmphasizeProperty().set(true);
                emphazedDescripteme = descripteme;
            }
        });
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, event -> {
            popup.hide();

            // de emphasize descripteme in modeling space
            if (emphazedDescripteme != null) {
                emphazedDescripteme.getEmphasizeProperty().set(false);
                emphazedDescripteme = null;
            }
        });
    }

    private  void setUpMenu() {
        menu = new ContextMenu();
        deleteAnnotationMenuItem = new MenuItem("Delete annotation");
        deleteAnnotationMenuItem.setOnAction(event -> {
            deleteAnnotation(userSelection.get().getStart());
        });
        //deleteAnnotationMenuItem.setDisable(true);
        menu.getItems().add(deleteAnnotationMenuItem);
        area.setContextMenu(menu);
        area.setContextMenuXOffset(0);
        area.setContextMenuYOffset(0);
        menu.setOnShowing(e -> {
            System.out.println("showing");
        });
        menu.setOnShown(e -> {
            System.out.println("shown");
        });

        MenuItem item1 = new MenuItem("Blue");
        item1.setOnAction(new EventHandler<>() {
            public void handle(ActionEvent e) {
                System.out.println("blue");
                if (!area.getSelectedText().isEmpty()) {
                    annotate(Color.BLUE,
                            userSelection.get().getStart(),
                            userSelection.get().getEnd());
                }
            }
        });
        menu.getItems().add(item1);
    }

    private void updateDescripteme() {
        Descripteme changedDescripteme = GlobalVariables.getGlobalVariables()
                .getDescriptemeChangedProperty().getValue();
        if (!GlobalVariables.getGlobalVariables().getAllDescriteme().contains(changedDescripteme)) {
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
        interviewText.getDescriptemesProperty().add(descripteme);
    }

    public void switchToAnalysisMode() {
        area.clearStyle(0, interviewText.getText().length() - 1);
    }
    public void switchToAnnotationMode() {
        /*
        for each annotation and descripteme, underline and highligth
         */
    }
}

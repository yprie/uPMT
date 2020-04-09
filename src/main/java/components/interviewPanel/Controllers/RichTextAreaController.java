package components.interviewPanel.Controllers;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.RemoveAnnotationCommand;
import components.interviewPanel.utils.LetterMap;
import components.interviewPanel.utils.TextStyle;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
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

import java.time.Duration;

public class RichTextAreaController {
    private InlineCssTextArea area;
    private InterviewText interviewText;
    private VirtualizedScrollPane<InlineCssTextArea> vsPane;
    private int userCaretPosition;
    private LetterMap letterMap;

    private ListChangeListener<Annotation> onAnnotationsChangeListener = change -> {
        System.out.println(change);
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

    public RichTextAreaController(InterviewText interviewText) {
        this.interviewText = interviewText;
        this.letterMap = new LetterMap();

        area = new InlineCssTextArea();
        area.setWrapText(true);
        area.setEditable(false);
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.appendText(interviewText.getText());
        area.setShowCaret(Caret.CaretVisibility.ON);

        setUpPopUp();
        setUpMouseEvent();

        this.interviewText.getAnnotationsProperty().addListener(
                new WeakListChangeListener<>(onAnnotationsChangeListener));
    }

    public VirtualizedScrollPane<InlineCssTextArea> getNode() {
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane(area);
        this.vsPane = vsPane;
        return vsPane;
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
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int chIdx = e.getCharacterIndex();
            Point2D pos = e.getScreenPosition();
            popupMsg.setText("Character '" + area.getText(chIdx, chIdx+1) + "' at " + pos);
            popup.show(area, pos.getX(), pos.getY() + 5);
        });
        area.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });
    }

    private void setUpMouseEvent() {
        area.setOnMousePressed(event -> {
            userCaretPosition = area.getCaretPosition();
            //System.out.println("area pressed " + userCaretPosition + " word: " + interviewText.getWordByIndex(userCaretPosition));
        });
    }

    public void setOnMouseReleased(EventHandler eventHandler) {
        area.setOnMouseReleased(eventHandler);
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

    public void annotate() {
        IndexRange selection = area.getSelection();
        if (selection.getStart() != selection.getEnd()) {
            Annotation annotation = new Annotation(interviewText,
                    selection.getStart(),
                    selection.getEnd(),
                    Color.YELLOW);
            new AddAnnotationCommand(interviewText, annotation).execute();
            // there is a listener that apply the style
            System.out.println(annotation);

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

    public void deselect() {
        area.deselect();
    }

    public void addDescripteme(Descripteme descripteme) {
        letterMap.becomeDescripteme(descripteme);
        applyStyle(descripteme);
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

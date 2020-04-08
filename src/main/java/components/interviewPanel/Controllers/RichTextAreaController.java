package components.interviewPanel.Controllers;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.utils.WordStyle;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.Pair;
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
import java.util.HashMap;

public class RichTextAreaController {
    private InlineCssTextArea area;
    private InterviewText interviewText;
    private VirtualizedScrollPane<InlineCssTextArea> vsPane;
    private int userCaretPosition;
    private HashMap<Pair<Integer, Integer>, WordStyle> wordMap;

    private ListChangeListener<Annotation> onAnnotationsChangeListener = change -> {
        System.out.println(change);
        while (change.next()) {
            for (Annotation removed : change.getRemoved()) {
                WordStyle style = wordMap.get(new Pair(removed.getStartIndex(), removed.getEndIndex()));
                System.out.println("on delete annotation, style : " + style);
                style.removeAnnotation();

                applyStyle(removed.getStartIndex(), removed.getEndIndex(), style);
            }
            for (Annotation added : change.getAddedSubList()) {
                WordStyle annotationStyle = added.getStyle();
                updateStyleFragment(added, annotationStyle);
            }
        }
    };

    public RichTextAreaController(InterviewText interviewText) {
        this.interviewText = interviewText;
        this.wordMap = new HashMap();

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

    private void updateStyleFragment(Fragment fragment, WordStyle previousStyle) {
        WordStyle style = previousStyle;
        WordStyle currentStyle = wordMap.get(new Pair(fragment.getStartIndex(), fragment.getEndIndex()));
        System.out.println("currentStyle:" + currentStyle);
        if (currentStyle != null) {
            style = currentStyle.mergeStyles(previousStyle);
        }
        System.out.println("finale style " + style);
        wordMap.put(new Pair(fragment.getStartIndex(), fragment.getEndIndex()), style);
        applyStyle(fragment.getStartIndex(), fragment.getEndIndex(), style);
    }

    private void applyStyle(int start, int end, WordStyle style) {
        String css = "";
        if (style.getIsAnnotation()) {
            css += "-rtfx-background-color: " + style.getCSSColor() + ";";
        }
        if (style.getIsDescripteme()) {
            css += "-rtfx-underline-color: black; " + "-rtfx-underline-width: 1.5;";
        }
        area.setStyle(start, end, css);
    }

    private Annotation createAnnotation(IndexRange selection) {
        int start = selection.getStart();
        int end = selection.getEnd();
        Annotation a = new Annotation(interviewText, start, end, Color.YELLOW);
        return a;
    }

    private void hideHighlightAnnotation(Annotation a) {
        WordStyle style = wordMap.get(new Pair(a.getStartIndex(), a.getEndIndex()));
        style.removeAnnotation();
        applyStyle(a.getStartIndex(), a.getEndIndex(), style);
    }

    public void annotate() {
        IndexRange selection = area.getSelection();
        if (selection.getStart() != selection.getEnd()) {
            Annotation a = createAnnotation(selection);
            area.deselect();
            System.out.println(a);

            new AddAnnotationCommand(interviewText, a).execute();
            System.out.println(a);
            wordMap.put(
                    new Pair<>(
                            a.getStartIndex(),
                            a.getEndIndex()),
                    new WordStyle(false, true, a.getColor())
            );
        }
    }

    public void deleteAnnotation() {
        Annotation a = interviewText.getFirstAnnotationByIndex(area.getCaretPosition());
        interviewText.removeAnnotation(a);
    }

    public String getSelectedText() {
        return area.getSelectedText();
    }

    public void deselect() {
        area.deselect();
    }

    public void addDescripteme(Descripteme descripteme) {
        WordStyle style = new WordStyle(true);
        wordMap.put(
                new Pair<>(
                    descripteme.getStartIndex(),
                    descripteme.getEndIndex()),
                style
        );
        System.out.println("new descripteme at " + descripteme.getStartIndex() + " and " + descripteme.getEndIndex());
        updateStyleFragment(descripteme, style);
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

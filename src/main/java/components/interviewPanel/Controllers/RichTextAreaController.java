package components.interviewPanel.Controllers;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import models.Annotation;
import models.Interview;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.Selection;
import org.fxmisc.richtext.SelectionImpl;
import org.fxmisc.richtext.event.MouseOverTextEvent;

import java.time.Duration;

public class RichTextAreaController {
    private InlineCssTextArea area;
    private Interview interview;

    private RichTextAreaController(Interview interview) {
        this.interview = interview;

        area = new InlineCssTextArea();
        area.setWrapText(true);
        area.setEditable(false);

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        sb.append(interview.getInterviewText().getText());
        for (int i = 0; i < 10; i++) {
            sb.append(i).append(" :").append(alphabet).append("\n");
        }
        area.appendText(sb.toString());

        setupRTFXSpecificCSSShapes();

        // select some other range with the regular caret/selection before showing area
        //area.selectRange(5, 0, 2, 4);

        area.setParagraphGraphicFactory(LineNumberFactory.get(area));

        setUpPupUp();

        area.setOnMouseClicked(e -> doMouseClicked(e));
        area.setOnMouseReleased(e -> doOnMouseReleased(e));

        //area.requestFocus();
    }

    public static Node createRichTextAreaController(Interview interview) {
        RichTextAreaController controller = new RichTextAreaController(interview);
        VirtualizedScrollPane<InlineCssTextArea> vsPane = new VirtualizedScrollPane(controller.area);
        return vsPane;
    }

    private void setUpPupUp() {
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

    private void highlightAnnotationBySelection(Annotation a) {
        Selection<String, String, String> extraSelection = new SelectionImpl<>("another selection" +
                a.getStartIndex() + a.getEndIndex(), area,
                path -> {
                    // make rendered selection path look like a yellow highlighter
                    path.setStrokeWidth(0);
                    path.setFill(a.getColor());
                }
        );
        if (!area.addSelection(extraSelection)) {
            throw new IllegalStateException("selection was not added to area");
        }
        // select something so it is visible
        extraSelection.selectRange(a.getStartIndex(), a.getEndIndex());
    }

    /**
     * Shows that RTFX-specific-CSS shapes are laid out in correct order, so that
     * selection and text and caret appears on top of them when made/moved
     */
    private void setupRTFXSpecificCSSShapes() {
        String background = "-rtfx-background-color: red; ";
        String underline = "-rtfx-underline-color: blue; " +
                "-rtfx-underline-width: 2.0;";
        String border = "-rtfx-border-stroke-color: green; " +
                "-rtfx-border-stroke-width: 3.0;";

        // set all of them at once on a give line to insure they display properly
        //area.setStyle(0, background + underline + border);

        // set each one over a given segment
        area.setStyle(3, 0, 5, underline);
        area.setStyle(5 , 0, 9, border);
    }

    private Annotation createAnnotation(IndexRange selection) {
        int start = selection.getStart();
        int end = selection.getEnd();
        Annotation a = new Annotation(interview.getInterviewText(), start, end, Color.YELLOW);
        return a;
    }

    private void highlightAnnotation(Annotation a) {
        String css = "-rtfx-background-color: " + a.getCSSColor();
        area.setStyle(a.getStartIndex(), a.getEndIndex(), css);
    }

    private void doMouseClicked(MouseEvent e) {
        System.out.println("OnMouseClicked");
    }

    private void doOnMouseReleased(MouseEvent e) {
        System.out.println("dOnMouseReleased");
        String selection = area.getSelectedText();
        System.out.println(selection);

        // DEBUG: encoding and line ending issue:
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();
        System.out.println(interview.getInterviewText().getText().substring(start, end));

        // Highlight selected text
        Annotation a = createAnnotation(area.getSelection());
        //highlightAnnotation(a); // Style way
        highlightAnnotationBySelection(a); // selection way

        area.deselect();
    }
}

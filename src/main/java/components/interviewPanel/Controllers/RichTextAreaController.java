package components.interviewPanel.Controllers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
    private VirtualizedScrollPane<InlineCssTextArea> vsPane;

    public RichTextAreaController(Interview interview) {
        this.interview = interview;

        area = new InlineCssTextArea();
        area.setWrapText(true);
        area.setEditable(false);
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.appendText(interview.getInterviewText().getText());
        //area.requestFocus();

        // example:
        setupRTFXSpecificCSSShapes();

        setUpPopUp();
        setUpDragAndDrop();
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

    private void setUpDragAndDrop() {
        Pane paneDragText = new Pane();
        paneDragText.setStyle("-fx-background-color:#f4f4f4;");
        paneDragText.setCursor(Cursor.MOVE);
        paneDragText.setOpacity(0.2);
    }

    public void setOnMouseReleased(EventHandler eventHandler) {
        area.setOnMouseReleased(eventHandler);
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

    public Annotation annotate() {
        System.out.println("annotate");
        String selection = area.getSelectedText();
        System.out.println(selection);

        // DEBUG: encoding and line ending issue:
        System.out.println("DEBUG");
        int start = area.getSelection().getStart();
        int end = area.getSelection().getEnd();
        System.out.println(interview.getInterviewText().getText().substring(start, end));
        // END DEBUG

        // Highlight selected text
        Annotation a = createAnnotation(area.getSelection());
        highlightAnnotation(a); // Style way
        //highlightAnnotationBySelection(a); // selection way

        area.deselect();
        return a;
    }

    public String getSelectedText() {
        return area.getSelectedText();
    }

    public void deselect() {
        area.deselect();
    }
}

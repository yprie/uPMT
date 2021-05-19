package components.interviewPanel.appCommands;

import components.interviewPanel.Controllers.InterviewTextController;
import components.interviewPanel.Controllers.RichTextAreaController;
import javafx.scene.control.IndexRange;
import models.Annotation;
import models.AnnotationColor;
import models.Descripteme;
import models.InterviewText;

public class InterviewTextCommandFactory {
    InterviewTextController interviewTextController;
    RichTextAreaController richTextAreaController;
    InterviewText interviewText;

    public InterviewTextCommandFactory(InterviewTextController interviewTextController,
                                       RichTextAreaController richTextAreaController,
                                       InterviewText interviewText) {
        this.interviewTextController = interviewTextController;
        this.richTextAreaController = richTextAreaController;
        this.interviewText = interviewText;
    }

    public AddAnnotationCommand getAddAnnotationCommand(IndexRange indexRange, AnnotationColor annotationColor) {
        return new AddAnnotationCommand(interviewText,
                new Annotation(interviewText,
                        indexRange.getStart(),
                        indexRange.getEnd(),
                        annotationColor.getColor()));
    }

    public DragSelectionCommand getDragSelectionCommand(IndexRange indexRange) {
        return new DragSelectionCommand(interviewTextController, richTextAreaController, indexRange);
    }

    public EraseAnnotationCommand getEraseAnnotationCommand(IndexRange indexRange) {
        return new EraseAnnotationCommand(interviewText, indexRange);
    }

    public RemoveAnnotationCommand getRemoveAnnotationCommand(Annotation annotation) {
        return new RemoveAnnotationCommand(interviewText, annotation, true);
    }

    public RevealDescriptemeCommand getRevealDescriptemeCommand(Descripteme descripteme) {
        return new RevealDescriptemeCommand(descripteme);
    }

    public CopyToClipboardCommand getCopyToClipboardCommand(String selectedText) {
        return new CopyToClipboardCommand(selectedText);
    }
}

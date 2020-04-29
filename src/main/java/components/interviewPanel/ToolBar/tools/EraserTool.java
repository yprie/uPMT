package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.RemoveAnnotationCommand;
import javafx.scene.control.IndexRange;
import models.Annotation;
import models.InterviewText;

import java.util.ArrayList;

public class EraserTool extends Tool {
    public EraserTool(String hexa, InterviewText interviewText) {
        super(hexa, interviewText);
    }

    @Override
    public void handle(IndexRange indexRange) {
        erase(interviewText, indexRange);
    }

    static void erase(InterviewText interviewText, IndexRange indexRange) {
        ArrayList<Annotation> annotationsInside = new ArrayList<>();
        for (int i = indexRange.getStart() ; i < indexRange.getEnd() ; i++) {
            Annotation annotation = interviewText.getFirstAnnotationByIndex(i);
            if (annotation != null && !annotationsInside.contains(annotation)) {
                annotationsInside.add(annotation);
                if (indexRange.getStart() <= annotation.getStartIndex()
                        && indexRange.getEnd() >= annotation.getEndIndex()) {
                    // the annotation is completely contained in the selection
                    new RemoveAnnotationCommand(interviewText, annotation).execute();
                }

                else if (indexRange.getStart() <= annotation.getStartIndex()
                        && indexRange.getEnd() < annotation.getEndIndex()) {
                    // the selection is over the beginning of the annotation
                    new RemoveAnnotationCommand(interviewText, annotation).execute();
                    new AddAnnotationCommand(interviewText, new Annotation(
                            interviewText,
                            indexRange.getEnd(),
                            annotation.getEndIndex(),
                            annotation.getColor())).execute();
                }
                else if (indexRange.getStart() > annotation.getStartIndex()
                        && indexRange.getEnd() >= annotation.getEndIndex()) {
                    // the selection is over the end of the annotation
                    new RemoveAnnotationCommand(interviewText, annotation).execute();
                    new AddAnnotationCommand(interviewText, new Annotation(
                            interviewText,
                            annotation.getStartIndex(),
                            indexRange.getStart(),
                            annotation.getColor())).execute();
                }

                else if (indexRange.getStart() > annotation.getStartIndex()
                        && indexRange.getEnd() < annotation.getEndIndex()) {
                    // the selection is inside the annotation
                    new RemoveAnnotationCommand(interviewText, annotation).execute();
                    new AddAnnotationCommand(interviewText, new Annotation(
                            interviewText,
                            annotation.getStartIndex(),
                            indexRange.getStart(),
                            annotation.getColor())).execute();
                    new AddAnnotationCommand(interviewText, new Annotation(
                            interviewText,
                            indexRange.getEnd(),
                            annotation.getEndIndex(),
                            annotation.getColor())).execute();
                }
            }
        }
    }
}

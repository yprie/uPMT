package components.interviewPanel.appCommands;

import application.history.HistoryManager;
import components.interviewPanel.ModelCommands.AddAnnotation;
import javafx.scene.control.IndexRange;
import models.Annotation;
import models.InterviewText;
import utils.command.Executable;

import java.util.ArrayList;

public class EraseAnnotationCommand implements Executable<Boolean> {
    InterviewText interviewText;
    IndexRange indexRange;

    public EraseAnnotationCommand(InterviewText interviewText, IndexRange indexRange) {
        this.interviewText = interviewText;
        this.indexRange = indexRange;
    }

    @Override
    public Boolean execute() {
        // return true if a new model user action command is executed
        Boolean newModelUserActionCommandExecuted = false;
        ArrayList<Annotation> annotationsInside = new ArrayList<>();
        for (int i = indexRange.getStart() ; i < indexRange.getEnd() ; i++) {
            Annotation annotation = interviewText.getAnnotationByIndex(i);
            if (annotation != null && !annotationsInside.contains(annotation)) {
                annotationsInside.add(annotation);
                if (indexRange.getStart() <= annotation.getStartIndex()
                        && indexRange.getEnd() >= annotation.getEndIndex()) {
                    // the annotation is completely contained in the selection
                    new RemoveAnnotationCommand(interviewText, annotation, !newModelUserActionCommandExecuted).execute();
                    newModelUserActionCommandExecuted = true;
                }

                else if (indexRange.getStart() <= annotation.getStartIndex()
                        && indexRange.getEnd() < annotation.getEndIndex()) {
                    // the selection is over the beginning of the annotation
                    new RemoveAnnotationCommand(interviewText, annotation, !newModelUserActionCommandExecuted).execute();
                    newModelUserActionCommandExecuted = true;
                    HistoryManager.addCommand(new AddAnnotation(interviewText, new Annotation(
                            interviewText,
                            indexRange.getEnd(),
                            annotation.getEndIndex(),
                            annotation.getColor())), false);
                }
                else if (indexRange.getStart() > annotation.getStartIndex()
                        && indexRange.getEnd() >= annotation.getEndIndex()) {
                    // the selection is over the end of the annotation
                    new RemoveAnnotationCommand(interviewText, annotation, !newModelUserActionCommandExecuted).execute();
                    newModelUserActionCommandExecuted = true;
                    HistoryManager.addCommand(new AddAnnotation(interviewText, new Annotation(
                            interviewText,
                            annotation.getStartIndex(),
                            indexRange.getStart(),
                            annotation.getColor())), false);
                }

                else if (indexRange.getStart() > annotation.getStartIndex()
                        && indexRange.getEnd() < annotation.getEndIndex()) {
                    // the selection is inside the annotation
                    new RemoveAnnotationCommand(interviewText, annotation, !newModelUserActionCommandExecuted).execute();
                    newModelUserActionCommandExecuted = true;
                    HistoryManager.addCommand(new AddAnnotation(interviewText, new Annotation(
                            interviewText,
                            annotation.getStartIndex(),
                            indexRange.getStart(),
                            annotation.getColor())), false);
                    HistoryManager.addCommand(new AddAnnotation(interviewText, new Annotation(
                            interviewText,
                            indexRange.getEnd(),
                            annotation.getEndIndex(),
                            annotation.getColor())), false);
                }
            }
        }
        return newModelUserActionCommandExecuted;
    }
}

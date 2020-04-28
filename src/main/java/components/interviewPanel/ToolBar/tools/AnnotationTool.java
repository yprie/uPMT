package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import models.Annotation;
import models.InterviewText;

public class AnnotationTool extends Tool {
    public AnnotationTool(String colorName, String hexa, InterviewText interviewText) {
        super(colorName, hexa, interviewText);
    }

    @Override
    public void handle(IndexRange indexRange) {
        EraserTool.erase(interviewText, indexRange);
        Annotation annotation = new Annotation(
                interviewText,
                indexRange.getStart(),
                indexRange.getEnd(),
                Color.web(hexa));
        new AddAnnotationCommand(interviewText, annotation).execute();
    }
}

package components.interviewPanel.ToolBar.tools;

import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.IndexRange;
import javafx.scene.paint.Color;
import models.Annotation;
import models.InterviewText;

public class AnnotationTool extends Tool {
    public AnnotationTool(String hexa, InterviewText interviewText, InterviewTextCommandFactory interviewPanelCommandFactory) {
        super(hexa, interviewText, interviewPanelCommandFactory);
    }

    @Override
    public void handle(IndexRange indexRange) {
        if (indexRange.getStart() != indexRange.getEnd()) {
            new AddAnnotationCommand(interviewText, new Annotation(
                    interviewText,
                    indexRange.getStart(),
                    indexRange.getEnd(),
                    Color.web(hexa))).execute();
        }
    }
}

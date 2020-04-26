package components.interviewPanel.ContextMenus;

import components.interviewPanel.ColorAnnotation;
import components.interviewPanel.Controllers.RichTextAreaController;
import components.interviewPanel.appCommands.AddAnnotationCommand;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import models.Annotation;
import models.InterviewText;

public class MenuItemFactory {

    private RichTextAreaController area; // ref to the rich text area
    private InterviewText interviewText; // ref to the interview Text

    MenuItemFactory(RichTextAreaController area, InterviewText interviewText) {
        this.area = area;
        this.interviewText = interviewText;
    }

    public MenuItem getYellowAnnotate(Annotation annotation) {
        MenuItem item = new MenuItem("Yellow");
        item.setOnAction(e -> {
            new AddAnnotationCommand(interviewText, new Annotation(
                    interviewText,
                    annotation.getStartIndex(),
                    annotation.getEndIndex(),
                    Color.web(ColorAnnotation.YELLOW))).execute();

        });
        return item;
    }

    /*
    public static MenuItem getEraser() {
        MenuItem deleteAnnotationMenuItem = new MenuItem("Delete annotation");
        deleteAnnotationMenuItem.setOnAction(event -> {
            Annotation annotation = interviewText.getFirstAnnotationByIndex(area.area.getCaretPosition());
            area.deleteAnnotation(annotation);
        });
        return deleteAnnotationMenuItem;
    }

     */
}

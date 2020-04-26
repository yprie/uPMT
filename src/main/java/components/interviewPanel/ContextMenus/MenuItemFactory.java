package components.interviewPanel.ContextMenus;

import components.interviewPanel.AnnotationColor;
import components.interviewPanel.Controllers.InterviewTextController;
import components.interviewPanel.Controllers.RichTextAreaController;
import components.interviewPanel.appCommands.AddAnnotationCommand;
import components.interviewPanel.appCommands.RemoveAnnotationCommand;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import models.Annotation;
import models.Descripteme;
import models.Fragment;
import models.InterviewText;

public class MenuItemFactory {

    private final InterviewTextController interviewTextController; // ref of the controller
    private final RichTextAreaController area; // ref to the rich text area
    private final InterviewText interviewText; // ref to the interview Text

    MenuItemFactory(InterviewTextController interviewTextController, RichTextAreaController area) {
        this.interviewTextController = interviewTextController;
        this.area = area;
        this.interviewText = interviewTextController.getInterviewText();
    }

    public MenuItem getAnnotate(String colorName, Fragment fragment) {
        MenuItem item = new MenuItem(colorName);
        item.setOnAction(e -> new AddAnnotationCommand(interviewText, new Annotation(
                fragment,
                AnnotationColor.getColor(colorName))
        ).execute());
        return item;
    }

    public MenuItem getAnnotate(InterviewText interviewText, String colorName, IndexRange selection) {
        MenuItem item = new MenuItem(colorName);
        item.setOnAction(e -> {
            new AddAnnotationCommand(interviewText, new Annotation(
                    interviewText,
                    selection.getStart(),
                    selection.getEnd(),
                    AnnotationColor.getColor(colorName))
            ).execute();
            area.deselect();

        });
        return item;
    }

    public MenuItem getEraser(Annotation annotation) {
        MenuItem item = new MenuItem("Delete annotation");
        item.setOnAction(event -> new RemoveAnnotationCommand(interviewText, annotation).execute());
        return item;
    }

    public MenuItem getCatch(IndexRange selection) {
        MenuItem item = new MenuItem("Catch");
        item.setOnAction(event -> {
            area.select(selection);
            interviewTextController.addPaneForDragAndDrop();
        });
        return item;
    }

    public MenuItem getReveal(Descripteme descripteme) {
        MenuItem item = new MenuItem("Reveal");
        item.setOnAction(event -> {
            descripteme.getEmphasizeProperty().set(!descripteme.getEmphasizeProperty().get());
        });
        return item;
    }
}

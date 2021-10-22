package components.interviewPanel.ContextMenus;

import application.configuration.Configuration;
import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import models.Annotation;
import models.AnnotationColor;
import models.Descripteme;

public class MenuItemFactory {

    private final InterviewTextCommandFactory interviewPanelCommandFactory;

    MenuItemFactory(InterviewTextCommandFactory interviewPanelCommandFactory) {
        this.interviewPanelCommandFactory = interviewPanelCommandFactory;
    }

    public MenuItem getAnnotate(AnnotationColor annotationColor, IndexRange selection) {
        MenuItem item = new MenuItem(Configuration.langBundle.getString(annotationColor.getName()));
        item.setOnAction(e -> {
            interviewPanelCommandFactory.getAddAnnotationCommand(selection, annotationColor).execute();
        });
        return item;
    }

    public MenuItem getEraser(Annotation annotation) {
        MenuItem item = new MenuItem(Configuration.langBundle.getString("delete_annotation"));
        item.setOnAction(event -> {
            interviewPanelCommandFactory.getRemoveAnnotationCommand(annotation).execute();
        });
        return item;
    }

    public MenuItem getCatch(IndexRange indexRange) {
        MenuItem item = new MenuItem(Configuration.langBundle.getString("catch"));
        item.setOnAction(event -> {
            interviewPanelCommandFactory.getDragSelectionCommand(indexRange).execute();
        });
        return item;
    }

    public MenuItem getReveal(Descripteme descripteme) {
        MenuItem item = new MenuItem(Configuration.langBundle.getString("reveal"));
        item.setOnAction(event -> {
            interviewPanelCommandFactory.getRevealDescriptemeCommand(descripteme).execute();
        });
        return item;
    }

    public MenuItem getCopyToClipboard(String selectedText) {
        MenuItem item = new MenuItem(Configuration.langBundle.getString("copy_to_clipboard"));
        item.setOnAction(event -> {
            interviewPanelCommandFactory.getCopyToClipboardCommand(selectedText).execute();
        });
        return item;
    }
}

package components.interviewPanel.ContextMenus;

import components.interviewPanel.AnnotationColor;
import components.interviewPanel.Controllers.InterviewTextController;
import components.interviewPanel.Controllers.RichTextAreaController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import models.Annotation;
import models.Descripteme;
import models.InterviewText;

import java.util.ArrayList;

public class ContextMenuFactory {

    MenuItemFactory menuItemFactory;

    public ContextMenuFactory(RichTextAreaController area, InterviewTextController interviewTextController) {
        menuItemFactory = new MenuItemFactory(interviewTextController, area);
    }

    public ContextMenu getContextMenuSelection(InterviewText interviewText, IndexRange selection) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(selection));
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(interviewText, colorName, selection));
        }
        return menu;
    }

    public ContextMenu getContextMenuAnnotation(Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(colorName, annotation));
        }
        menu.getItems().add(menuItemFactory.getEraser(annotation));
        return menu;
    }

    public ContextMenu getContextMenuDescriptemeAndAnnotation(ArrayList<Descripteme> descriptemes, Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        return menu;
    }

    /*
    public static ContextMenu getContextMenuSelection() {

    }

     */

}

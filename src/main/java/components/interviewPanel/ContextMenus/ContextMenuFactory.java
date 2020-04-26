package components.interviewPanel.ContextMenus;

import components.interviewPanel.AnnotationColor;
import components.interviewPanel.Controllers.RichTextAreaController;
import javafx.scene.control.ContextMenu;
import models.Annotation;
import models.Descripteme;
import models.InterviewText;

import java.util.ArrayList;

public class ContextMenuFactory {

    MenuItemFactory menuItemFactory;

    public ContextMenuFactory(RichTextAreaController area, InterviewText interviewText) {
        menuItemFactory = new MenuItemFactory(area, interviewText);
    }

    public ContextMenu getContextMenuAnnotation(Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(colorName, annotation));
        }
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

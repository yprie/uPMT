package components.interviewPanel.ContextMenus;

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

    public ContextMenu getContextMenuDescriptemeAndAnnotation(ArrayList<Descripteme> descriptemes, Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getYellowAnnotate(annotation));
        return menu;
    }

    /*
    public static ContextMenu getContextMenuSelection() {

    }

     */

}

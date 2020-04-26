package components.interviewPanel.ContextMenus;

import components.interviewPanel.AnnotationColor;
import components.interviewPanel.Controllers.InterviewTextController;
import components.interviewPanel.Controllers.RichTextAreaController;
import javafx.scene.control.*;
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
        menu.getItems().add(new SeparatorMenuItem());
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(interviewText, colorName, selection));
        }
        return menu;
    }

    public ContextMenu getContextMenuAnnotation(Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(colorName, annotation));
        }
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menuItemFactory.getEraser(annotation));
        return menu;
    }

    public ContextMenu getContextMenuDescriptemeAndAnnotation(ArrayList<Descripteme> descriptemes, Annotation annotation) {
        ContextMenu menu = new ContextMenu();

        // For descriptemes
        for (Descripteme descripteme : descriptemes) {
            Menu subMenu = new Menu(descripteme.getCroppedFragmentText());
            subMenu.getItems().add(menuItemFactory.getReveal(descripteme));
            subMenu.getItems().add(new SeparatorMenuItem());
            for (String colorName : AnnotationColor.getNames()) {
                subMenu.getItems().add(menuItemFactory.getAnnotate(colorName, descripteme));
            }
            menu.getItems().add(subMenu);
        }
        menu.getItems().add(new SeparatorMenuItem());

        // For the annotation
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        for (String colorName : AnnotationColor.getNames()) {
            menu.getItems().add(menuItemFactory.getAnnotate(colorName, annotation));
        }
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menuItemFactory.getEraser(annotation));


        return menu;
    }

    public ContextMenu getContextMenuDescripteme(ArrayList<Descripteme> descriptemes) {
        ContextMenu menu = new ContextMenu();
        for (Descripteme descripteme : descriptemes) {
            Menu subMenu = new Menu(descripteme.getCroppedFragmentText());
            subMenu.getItems().add(menuItemFactory.getReveal(descripteme));
            subMenu.getItems().add(new SeparatorMenuItem());
            for (String colorName : AnnotationColor.getNames()) {
                subMenu.getItems().add(menuItemFactory.getAnnotate(colorName, descripteme));
            }
            menu.getItems().add(subMenu);
        }
        return menu;
    }
}

package components.interviewPanel.ContextMenus;

import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import models.Annotation;
import models.AnnotationColor;
import models.Descripteme;

import java.util.ArrayList;
import java.util.List;

public class ContextMenuFactory {

    MenuItemFactory menuItemFactory;
    List<AnnotationColor> annotationColorList;

    public ContextMenuFactory(InterviewTextCommandFactory interviewPanelCommandFactory, List<AnnotationColor> annotationColorList) {
        menuItemFactory = new MenuItemFactory(interviewPanelCommandFactory);
        this.annotationColorList = annotationColorList;
    }

    public ContextMenu getContextMenuSelection(IndexRange selection) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(selection));
        menu.getItems().add(new SeparatorMenuItem());
        annotationColorList.forEach(annotationColor -> {
            menu.getItems().add(menuItemFactory.getAnnotate(annotationColor, selection));
        });
        return menu;
    }

    public ContextMenu getContextMenuAnnotation(Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        for (AnnotationColor annotationColor : annotationColorList) {
            menu.getItems().add(menuItemFactory.getAnnotate(annotationColor, annotation.getIndexRange()));
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
            for (AnnotationColor annotationColor : annotationColorList) {
                subMenu.getItems().add(menuItemFactory.getAnnotate(annotationColor, descripteme.getIndexRange()));
            }
            menu.getItems().add(subMenu);
        }
        menu.getItems().add(new SeparatorMenuItem());

        // For the annotation
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        for (AnnotationColor annotationColor : annotationColorList) {
            menu.getItems().add(menuItemFactory.getAnnotate(annotationColor, annotation.getIndexRange()));
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
            for (AnnotationColor annotationColor: annotationColorList) {
                subMenu.getItems().add(menuItemFactory.getAnnotate(annotationColor, descripteme.getIndexRange()));
            }
            menu.getItems().add(subMenu);
        }
        return menu;
    }
}

package components.interviewPanel.ContextMenus;

import components.interviewPanel.appCommands.InterviewTextCommandFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
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

    public ContextMenu getContextMenuSelection(String selectedText, IndexRange selection) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(selection));
        menu.getItems().add(new SeparatorMenuItem());
        addMenuAnnotationColorList(menu.getItems(), selection);
        addMenuCopyToClipboard(menu, selectedText);
        return menu;
    }

    public ContextMenu getContextMenuAnnotation(String selectedText, Annotation annotation) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        addMenuAnnotationColorList(menu.getItems(), annotation.getIndexRange());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menuItemFactory.getEraser(annotation));
        addMenuCopyToClipboard(menu, selectedText);
        return menu;
    }

    public ContextMenu getContextMenuDescriptemeAndAnnotation(String selectedText, ArrayList<Descripteme> descriptemes, Annotation annotation) {
        ContextMenu menu = new ContextMenu();

        // For descriptemes
        addMenuDescripteme(menu, descriptemes);

        menu.getItems().add(new SeparatorMenuItem());

        // For the annotation
        menu.getItems().add(menuItemFactory.getCatch(annotation.getIndexRange()));
        menu.getItems().add(new SeparatorMenuItem());
        addMenuAnnotationColorList(menu.getItems(), annotation.getIndexRange());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menuItemFactory.getEraser(annotation));
        addMenuCopyToClipboard(menu, selectedText);
        return menu;
    }

    public ContextMenu getContextMenuDescripteme(String selectedText, ArrayList<Descripteme> descriptemes) {
        ContextMenu menu = new ContextMenu();
        addMenuDescripteme(menu, descriptemes);
        addMenuCopyToClipboard(menu, selectedText);
        return menu;
    }

    private void addMenuDescripteme(ContextMenu menu, ArrayList<Descripteme> descriptemes) {
        for (Descripteme descripteme : descriptemes) {
            Menu subMenu = new Menu(descripteme.getCroppedFragmentText());
            subMenu.getItems().add(menuItemFactory.getReveal(descripteme));
            subMenu.getItems().add(new SeparatorMenuItem());
            addMenuAnnotationColorList(subMenu.getItems(), descripteme.getIndexRange());
            menu.getItems().add(subMenu);
        }
    }

    private void addMenuAnnotationColorList(ObservableList<MenuItem> menuItems, IndexRange selection) {
        annotationColorList.forEach(annotationColor -> {
            menuItems.add(menuItemFactory.getAnnotate(annotationColor, selection));
        });
    }

    private void addMenuCopyToClipboard(ContextMenu menu, String selectedText) {
        if (!selectedText.isEmpty()) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menuItemFactory.getCopyToClipboard(selectedText));
        }
    }
}

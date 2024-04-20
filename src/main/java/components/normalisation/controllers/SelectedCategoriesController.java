package components.normalisation.controllers;

import components.normalisation.SelectedCategoriesView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.CategoryRowModel;
import models.Interview;
import models.Moment;

import java.net.URL;
import java.util.*;

public class SelectedCategoriesController implements Initializable {

    @FXML
    private GridPane selectedCategoriesGrid;
    private final ObservableList<CategoryRowModel> categories = FXCollections.observableArrayList();
    private final Map<CategoryRowModel, Integer> categoryRowMap = new HashMap<>();

    private final Map<Pair<Interview,Moment>, Integer> interviewMomentColumnMap = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTable(categories);
    }

    // Méthode pour remplir le tableau avec les en-têtes d'interview, de moments et les instances de catégorie
    public void fillTable(List<CategoryRowModel> categories) {
        selectedCategoriesGrid.getChildren().clear();
        addCategoryLabels(categories); // Ajouter les noms des catégories dans la première colonne à partir de la troisième ligne

        int columnIndex = 1;

        // Ajouter les en-têtes d'interview et de moments
        for (CategoryRowModel category : categories) {
            for (Interview interview : category.getInterviews()) {

                List<Moment> moments = category.getInterviewMomentsMap().get(interview);
                for (Moment moment : moments) {
                    interviewMomentColumnMap.put(new Pair<>(interview, moment), columnIndex);
                    Label interviewLabel = createLabel(interview.getTitle());
                    interviewLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 5px;");
                    selectedCategoriesGrid.add(interviewLabel, columnIndex, 0);
                    Label momentLabel = createLabel(moment.getName());
                    momentLabel.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");
                    selectedCategoriesGrid.add(momentLabel, columnIndex, 1);
                    columnIndex++;
                }
            }
        }

        addCategoryInstances(categories); // Ajouter les instances des catégories par interview
        // Ajouter les lignes de la grille du tableau
        selectedCategoriesGrid.setStyle("-fx-grid-lines-visible: true;");
    }

    // Méthode pour ajouter les noms des catégories dans la première colonne à partir de la troisième ligne
    private void addCategoryLabels(List<CategoryRowModel> categories) {
        int rowIndex = 2;
        for (CategoryRowModel category : categories) {
            categoryRowMap.put(category, rowIndex);
            Label categoryNameLabel = createLabel(category.getName());
            categoryNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 5px;");
            selectedCategoriesGrid.add(categoryNameLabel, 0, rowIndex);
            rowIndex++;
        }
    }

    // Méthode pour ajouter les instances des catégories par interview
    // Méthode pour ajouter les instances des catégories par interview
    private void addCategoryInstances(List<CategoryRowModel> categories) {
        int columnIndex;
        int rowIndex = 2;
        List<Map<Interview, List<Moment>>> interviewMomentsMaps = new ArrayList<>();
        for (CategoryRowModel category : categories) {
            interviewMomentsMaps.add(category.getInterviewMomentsMap());
        }
        // Naviguer dans tous les moments de chaque interview, si ce moment contient l'une des catégories de la liste, on ajoute l'instance de la catégorie dans la cellule correspondante (pour la ligne on utilise la map categoryRowMap)
        // ça permet de ne pas boucler sur les categories et de prendre bien tout en compte
        for (Map<Interview, List<Moment>> interviewMomentsMap : interviewMomentsMaps) {
            columnIndex = 1;
            for (Map.Entry<Interview, List<Moment>> entry : interviewMomentsMap.entrySet()) {
                Interview interview = entry.getKey();
                List<Moment> moments = entry.getValue();
                for (Moment moment : moments) {
                    for (CategoryRowModel category : categories) {
                        if (moment.containsCategory(category.getCategory())) {
                            Label categoryInstanceLabel = createLabel(category.getConcreteCategory(moment).toString());
                            addDragDropHandlers(categoryInstanceLabel);
                            selectedCategoriesGrid.add(categoryInstanceLabel,interviewMomentColumnMap.get(new Pair<>(interview, moment)), categoryRowMap.get(category));
                        }
                    }
                    columnIndex++;
                }
            }
        }

    }


    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 5px;");
        return label;
    }

    private void addDragDropHandlers(Label label) {
        label.setOnDragDetected(event -> {
            System.out.println("Drag detected on label: " + label.getText());
            Dragboard dragboard = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            dragboard.setContent(content);
            label.setStyle(label.getStyle() + "-fx-background-color: lightblue;");
        });

        label.setOnDragOver(event -> {
            System.out.println("Drag over label: " + label.getText());
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        label.setOnDragDropped(event -> {
            System.out.println("Drag dropped on label: " + label.getText());
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                // Récupérer le texte déposé
                String droppedText = dragboard.getString();
                System.out.println("Dropped text: " + droppedText);

                // Supprimer le contenu de la cellule où l'étiquette a été déposée
                Node target = event.getPickResult().getIntersectedNode();
                if (target != null && target instanceof Label) {
                    ((Label) target).setText(droppedText);
                    System.out.println("Dropped text set on label: " + droppedText);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        label.setOnDragDone(event -> {
            System.out.println("Drag done on label: " + label.getText());
            label.setStyle(label.getStyle().replace("-fx-background-color: lightblue;", ""));
            event.consume();
        });
    }

    public void launchView(List<CategoryRowModel> categories) {
        Stage stage = new Stage();
        SelectedCategoriesView selectedCategoriesView = new SelectedCategoriesView();
        selectedCategoriesView.setCategories(categories);

        try {
            selectedCategoriesView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCategories(List<CategoryRowModel> categories) {
        this.categories.addAll(categories);
    }
}

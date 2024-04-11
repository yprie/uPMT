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
import models.CategoryRowModel;
import models.Moment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectedCategoriesController implements Initializable {

    @FXML
    private GridPane selectedCategoriesGrid;
    private final ObservableList<CategoryRowModel> categories = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTable(categories);
    }

    public void fillTable(List<CategoryRowModel> categories) {
        selectedCategoriesGrid.getChildren().clear();
        selectedCategoriesGrid.add(createHeaderLabel("Nom de la catégorie"), 0, 0);
        selectedCategoriesGrid.setGridLinesVisible(true);
        selectedCategoriesGrid.setStyle("-fx-alignment: center;");

        int columnIndex = 1;
        int rowIndex = 1;

        for (CategoryRowModel category : categories) {
            Label categoryNameLabel = createLabel(category.getName());
            categoryNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 5px;");

            selectedCategoriesGrid.add(categoryNameLabel, 0, rowIndex);

            for (Moment moment : category.getMoments()) {
                Label momentLabel = createLabel(moment.getName());
                momentLabel.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");
                selectedCategoriesGrid.add(momentLabel, columnIndex, 0);
                columnIndex++;
            }

            rowIndex++;
        }

        columnIndex = 1;
        rowIndex = 1;
        for (CategoryRowModel category : categories) {
            for (Moment moment : category.getMoments()) {
                Label instanceLabel = createLabel(category.getConcreteCategory(moment).toString());
                instanceLabel.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");
                addDragDropHandlers(instanceLabel);
                selectedCategoriesGrid.add(instanceLabel, columnIndex, rowIndex);
                columnIndex++;
            }
            rowIndex++;
            columnIndex = 1;
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-padding: 5px;");
        return label;
    }

    private Label createHeaderLabel(String text) {
        Label label = createLabel(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 5px;");
        return label;
    }

    private void addDragDropHandlers(Label label) {
        label.setOnDragDetected(event -> {
            System.out.println("Drag detected on label: " + label.getText());
            Dragboard dragboard = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            dragboard.setContent(content);
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

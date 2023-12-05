package components.modelisationSpace.search;

import components.interviewPanel.search.ButtonSearchType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import models.Moment;
import org.fxmisc.richtext.InlineCssTextArea;
import utils.ModelisationNavigator;
import utils.MomentSearch;
import utils.SearchResult;

public class MomentSearchHandler implements EventHandler<ActionEvent> {

    private final String HIGHLIGHT_STYLE = "-fx-background-color:" + "#fdff32" + "; -fx-fill:" + "#fdff32" + ";";
    private ButtonSearchType type;
    private MomentSearch searchResult;
    private ScrollPane scrollPane;
    private AnchorPane anchorPane;
    private Node previousNode;
    ModelisationNavigator modelisationNavigator;

    public MomentSearchHandler(ButtonSearchType type, MomentSearch searchResult, ScrollPane scrollPane, AnchorPane anchorPane) {
        this.type = type;
        this.searchResult = searchResult;
        this.scrollPane = scrollPane;
        this.anchorPane = anchorPane;
        this.modelisationNavigator = new ModelisationNavigator(this.scrollPane, this.anchorPane);
    }

    @Override
    public void handle(ActionEvent event) {
        if (this.searchResult.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Find");
            alert.setHeaderText("Find");
            alert.setContentText("No matches found.");
            alert.showAndWait();
            event.consume(); // prevent the dialog from closing
            return;
        }
        if (previousNode != null) {
            this.removeHighlight(previousNode);
        }
        Node currentNode;
        if (this.type.equals(ButtonSearchType.NEXT)) {
            currentNode = this.searchResult.getNextResult();
        } else {
            currentNode = this.searchResult.getPreviousResult();
        }
        previousNode = currentNode;
        scrollPane.requestFocus();
        this.modelisationNavigator.centerNodeInScrollPaneX(currentNode);
        this.modelisationNavigator.centerNodeInScrollPaneY(currentNode);

        String initialNodeStyle = currentNode.getStyle();
        currentNode.setStyle(initialNodeStyle + HIGHLIGHT_STYLE);

        event.consume(); // prevent the dialog from closing
    }

    public void removeHighlight(Node node) {
        String nodeStyle = node.getStyle();
        node.setStyle(nodeStyle.replace(HIGHLIGHT_STYLE, ""));
    }

    public void cleanSearchHighlight() {
        if (previousNode != null) {
            this.removeHighlight(previousNode);
        }
    }
}

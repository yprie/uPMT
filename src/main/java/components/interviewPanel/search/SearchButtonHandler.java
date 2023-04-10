package components.interviewPanel.search;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import models.SearchResult;
import org.fxmisc.richtext.InlineCssTextArea;

public class SearchButtonHandler implements EventHandler<ActionEvent> {

    private ButtonSearchType type;
    private SearchResult searchResult;
    private InlineCssTextArea richTextArea;

    public SearchButtonHandler(ButtonSearchType type, SearchResult searchResult, InlineCssTextArea richTextArea) {
        this.type = type;
        this.searchResult = searchResult;
        this.richTextArea = richTextArea;
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
        int currentPosition;

        if (this.type.equals(ButtonSearchType.NEXT)) {
            currentPosition = this.searchResult.getNextResult();
        } else {
            currentPosition = this.searchResult.getPreviousResult();
        }
        richTextArea.requestFocus();
        richTextArea.moveTo(currentPosition);
        richTextArea.requestFollowCaret();
        richTextArea.selectRange(currentPosition, currentPosition + this.searchResult.getCurrentSearchWord().length());
        event.consume(); // prevent the dialog from closing
    }
}
package components.modelisationSpace.UI;

import javafx.geometry.Side;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import utils.AutoSuggestions;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

// https://stackoverflow.com/a/40369435

public class AutoSuggestionsTextField extends TextField {
    //Local variables
    //entries to autocomplete
    private final SortedSet<String> entries;
    //popup GUI
    private ContextMenu entriesPopup;


    public AutoSuggestionsTextField() {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        setListner();
    }

    /**
     * "Suggestion" specific listners
     */
    private void setListner() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            searchSuggestions();
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.getItems().clear();
            fetchEntries();
            searchSuggestions();
            if (!newValue) {
                hide();
            }
            else {
                show();
            }
        });
    }

    private void show() {
        entriesPopup.show(AutoSuggestionsTextField.this, Side.BOTTOM, 0, 0); //position of popup
    }

    private void hide() {
        entriesPopup.hide();
    }

    private void fetchEntries() {
        entries.addAll(AutoSuggestions.getAutoSuggestions().getSuggestions());
    }

    private void searchSuggestions() {
        String enteredText = getText();
        //always hide suggestion if nothing has been entered (only "spacebars" are dissalowed in TextField)
        if (enteredText == null || enteredText.isEmpty()) {
            entriesPopup.hide();
        } else {
            //filter all possible suggestions depends on "Text", case insensitive
            List<String> filteredEntries = entries.stream()
                    .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                    .collect(Collectors.toList());
            //some suggestions are found
            if (!filteredEntries.isEmpty()) {
                //build popup - list of "CustomMenuItem"
                populatePopup(filteredEntries, enteredText);
                if (!entriesPopup.isShowing()) { //optional
                    show();
                }
                //no suggestions -> hide
            } else {
                hide();
            }
        }
    }


    /**
     * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult, String searchReauest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        //int maxEntries = 10;
        int maxEntries = 50;
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label(result);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                setText(result);
                positionCaret(result.length());
                entriesPopup.hide();
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().addAll(menuItems);
    }


    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public SortedSet<String> getEntries() {
        return entries;
    }

}

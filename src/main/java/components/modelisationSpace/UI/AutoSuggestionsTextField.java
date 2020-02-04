package components.modelisationSpace.UI;

import utils.autoSuggestion.AutoSuggestions;
import components.schemaTree.Cell.SchemaTreePluggable;

import javafx.geometry.Side;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;

import java.util.*;


// https://stackoverflow.com/a/40369435

public class AutoSuggestionsTextField extends TextField {
    //Local variables
    //entries to autocomplete
    private final SortedSet<String> entries;

    // entries map where key are element name (String) and value are the elements (SchemaTreePluggable)
    private Map<String, SchemaTreePluggable> suggestions;
    //popup GUI
    private ContextMenu entriesPopup;


    public AutoSuggestionsTextField() {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();

        setListener();
    }

    /**
     * "Suggestion" specific listeners
     */
    private void setListener() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            showSuggestions();
        });

        //Hide always by focus-in (optional) and out
        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                showSuggestions();
            }
        });
    }

    private void show() {
        entriesPopup.show(AutoSuggestionsTextField.this, Side.BOTTOM, 0, 0); //position of popup
    }

    private void hide() {
        entriesPopup.hide();
    }

    private void showSuggestions() {
        String enteredText = getText();
        if (enteredText == null || enteredText.isEmpty()) {
            hide();
        }
        else {
            this.suggestions = AutoSuggestions.getAutoSuggestions().getStrategy().fetchSuggestions();

            // Populate the list of possibilities
            entries.clear();
            Set<Map.Entry<String, SchemaTreePluggable>> setHm = suggestions.entrySet();
            Iterator<Map.Entry<String, SchemaTreePluggable>> it = setHm.iterator();
            while(it.hasNext()){
                Map.Entry<String, SchemaTreePluggable> e = it.next();
                System.out.print(e.getKey());
                entries.add(e.getKey());
            }

            // filter the list of result with the entered text
            List<String> searchResult = AutoSuggestions.getAutoSuggestions().searchSuggestions(enteredText, entries);
            populatePopup(searchResult);
            show();
        }
    }

    /**
     * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult) {
        entriesPopup.getItems().clear();
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 50; // TODO: chose a value
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
                onClick(result);
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().addAll(menuItems);
    }

    private void onClick(String result) {
        setText(result);
        positionCaret(result.length());
        hide();
        SchemaTreePluggable selectedElement = suggestions.get(result);
    }

    private void onEnter() {
        // On press Enter: create a category
        /*
        SchemaTreeCommandFactory cmdFactory = new SchemaTreeCommandFactory(AutoSuggestions.getAutoSuggestions(), getTreeItem());
        SchemaCategory newModel = new SchemaCategory(result);
        cmdFactory.addSchemaTreeChild(newModel).execute();
        */
    }
}

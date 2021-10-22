package utils.autoSuggestion;

import components.schemaTree.Cell.SchemaTreePluggable;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import utils.autoSuggestion.strategies.SuggestionStrategy;

import java.util.*;


// https://stackoverflow.com/a/40369435

public class AutoSuggestionsTextField extends TextField {
    // Local variables

    // Entries map where key are element name (String) and value are the elements (SchemaTreePluggable)
    private Map<String, SchemaTreePluggable> suggestions;

    // Popup GUI
    private ContextMenu entriesPopup;

    // Suggestion Strategy
    private SuggestionStrategy suggestionStrategy;

    //Flag to avoid suggestions to pop up after selected, (because of Run Later ...)
    private boolean canAppear;

    public AutoSuggestionsTextField() {
        super();
        this.entriesPopup = new ContextMenu();
        this.suggestionStrategy = null;
        canAppear = true;
        setListener();
    }

    public AutoSuggestionsTextField(SuggestionStrategy suggestionStrategy) {
        super();
        this.entriesPopup = new ContextMenu();
        this.suggestionStrategy = suggestionStrategy;
        canAppear = true;
        setListener();
    }

    public AutoSuggestionsTextField(String text) {
        super();
        this.entriesPopup = new ContextMenu();
        this.suggestionStrategy = null;
        this.setText(text);
        canAppear = true;
        setListener();
    }

    public AutoSuggestionsTextField(String text, SuggestionStrategy autoSuggestionStrategy) {
        super();
        this.entriesPopup = new ContextMenu();
        this.suggestionStrategy = autoSuggestionStrategy;
        canAppear = true;
        this.setText(text);
        setListener();
    }



    public void setStrategy(SuggestionStrategy newSuggestionStrategy) {
        suggestionStrategy = newSuggestionStrategy;
    }

    /**
     * "Suggestion" specific listeners
     */
    private void setListener() {
        //Add "suggestions" by changing text
        textProperty().addListener((observable, oldValue, newValue) -> {
            if(Math.abs(newValue.length() - oldValue.length()) >= 1 && (oldValue.length() != 0 || newValue.length() == 1) && canAppear)
                showSuggestions();
            else
                hide();
        });
    }

    private void show() {
        Platform.runLater(()-> {
            entriesPopup.show(AutoSuggestionsTextField.this, Side.BOTTOM, 0, 0); //position of popup
        });
    }

    private void hide() {
        entriesPopup.hide();
    }

    private void showSuggestions() {
        suggestions = suggestionStrategy.getSuggestions(getText());
        populatePopup(suggestions);
        show();
    }

    /**
     * Populate the entry set with the given search results. Display is limited to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(Map<String, SchemaTreePluggable> searchResult) {
        entriesPopup.getItems().clear();
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 50; // TODO: chose a value
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (Map.Entry<String, SchemaTreePluggable> entry : searchResult.entrySet()) {
            //label with graphic (text flow) to highlight founded subtext in suggestions
            Label entryLabel = new Label(entry.getKey());
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            //if any suggestion is select set it into text and close popup
            item.setOnAction(actionEvent -> {
                onClick(entry.getKey());
            });
        }

        //"Refresh" context menu
        entriesPopup.getItems().addAll(menuItems);
    }

    private void onClick(String result) {
        canAppear = false;
        setText(result);
        canAppear = true;
        positionCaret(result.length());
        hide();
        Object selectedElement = suggestions.get(result);
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

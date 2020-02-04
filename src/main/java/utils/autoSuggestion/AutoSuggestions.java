package utils.autoSuggestion;

import components.schemaTree.Cell.Models.SchemaTreeRoot;
import utils.autoSuggestion.strategies.SuggestionStrategy;

import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class AutoSuggestions {

    private static final AutoSuggestions autoSuggestions = new AutoSuggestions();

    private SchemaTreeRoot root;

    private SuggestionStrategy suggestionStrategy;

    private AutoSuggestions() {} // private constructor

    public static AutoSuggestions getAutoSuggestions() {
        return autoSuggestions;
    }

    public void setSchemaTreeRoot(SchemaTreeRoot root) {
        this.root = root;
    }
    public SchemaTreeRoot getSchemaTreeRoot() {
        return root;
    }

    public void setStrategy(SuggestionStrategy newSuggestionStrategy) {
        suggestionStrategy = newSuggestionStrategy;
        suggestionStrategy.setSchemaTreeRoot(root);
    }
    public SuggestionStrategy getStrategy() {
        return suggestionStrategy;
    }

    public List<String> searchSuggestions(String enteredText, SortedSet<String> entries) {
        //filter all possible suggestions depends on "Text", case insensitive
        List<String> searchResult = entries.stream()
                .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                .collect(Collectors.toList());
        return searchResult;
    }
}

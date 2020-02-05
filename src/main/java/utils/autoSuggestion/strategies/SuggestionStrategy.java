package utils.autoSuggestion.strategies;

import components.schemaTree.Cell.SchemaTreePluggable;

import java.util.HashMap;
import java.util.Map;

public abstract class SuggestionStrategy {
    // TODO: SchemaTreePluggable: it could be one day another type than this
    public abstract Map<String, SchemaTreePluggable> fetchSuggestions();

    Map<String, SchemaTreePluggable> filter(Map<String, SchemaTreePluggable> entries, String enteredText) {
        Map<String, SchemaTreePluggable> filteredResult = new HashMap<>();
        if (!enteredText.equals("")) {
            for (Map.Entry<String, SchemaTreePluggable> entry : entries.entrySet()) {
                if (entry.getKey().toLowerCase().contains(enteredText.toLowerCase())) {
                    filteredResult.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return filteredResult;
    }

    public Map<String, SchemaTreePluggable> getSuggestions(String enteredText) {
        return filter(fetchSuggestions(), enteredText);
    }
}

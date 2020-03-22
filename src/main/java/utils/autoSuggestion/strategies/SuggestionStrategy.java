package utils.autoSuggestion.strategies;

import java.util.HashMap;
import java.util.Map;

public abstract class SuggestionStrategy<T> {

    public abstract Map<String, T> fetchSuggestions();

    Map<String, T> filter(Map<String, T> entries, String enteredText) {
        Map<String, T> filteredResult = new HashMap<>();
        if (!enteredText.equals("")) {
            for (Map.Entry<String, T> entry : entries.entrySet()) {
                if (entry.getKey().toLowerCase().contains(enteredText.toLowerCase())) {
                    filteredResult.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return filteredResult;
    }

    public Map<String, T> getSuggestions(String enteredText) {
        return filter(
                fetchSuggestions(),
                enteredText
        );
    }
}

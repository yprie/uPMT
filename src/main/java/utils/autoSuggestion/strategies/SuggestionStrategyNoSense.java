package utils.autoSuggestion.strategies;

// This is just to test and debug the suggestion strategy design pattern

import utils.autoSuggestion.SuggestionNoSense;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyNoSense extends SuggestionStrategy<SuggestionNoSense> {
    public Map<String, SuggestionNoSense> fetchSuggestions() {
        Map<String, SuggestionNoSense> result = new HashMap<>();
        result.put("NoSens 1", new SuggestionNoSense("NoSens 1"));
        result.put("NoSens 2", new SuggestionNoSense("NoSens 2"));
        result.put("NoSens 3", new SuggestionNoSense("NoSens 3"));
        return result;
    }
}

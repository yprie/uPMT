package utils.autoSuggestion.strategies;

// This is just to test and debug the suggestion strategy design pattern

import utils.autoSuggestion.FakeSuggestion;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyNoSense extends SuggestionStrategy<FakeSuggestion> {
    public Map<String, FakeSuggestion> fetchSuggestions() {
        Map<String, FakeSuggestion> result = new HashMap<>();
        result.put("NoSens 1", new FakeSuggestion("NoSens 1"));
        result.put("NoSens 2", new FakeSuggestion("NoSens 2"));
        result.put("NoSens 3", new FakeSuggestion("NoSens 3"));
        return result;
    }
}

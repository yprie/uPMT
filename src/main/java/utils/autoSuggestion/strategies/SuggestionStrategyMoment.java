package utils.autoSuggestion.strategies;

import components.modelisationSpace.moment.model.Moment;
import utils.autoSuggestion.AutoSuggestions;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyMoment extends SuggestionStrategy<Moment> {

    private Map<String, Moment> result = new HashMap<>();

    @Override
    public Map<String, Moment> fetchSuggestions() {
        // Create a list with all the possibilities: the list where we search the entered text for a match
        Map<String, Moment> result = new HashMap<>();
        for(Moment subMoment: AutoSuggestions.getRootMoment().momentsProperty()) {
            result.put(subMoment.getName(), subMoment);
            iterateOverSubMoment(subMoment, result);
        }
        return result;
    }

    private void iterateOverSubMoment(Moment moment, Map<String, Moment> result) {
        for(Moment subMoment: moment.momentsProperty()) {
            result.put(subMoment.getName(), subMoment);
            iterateOverSubMoment(subMoment, result);
        }
    }
}

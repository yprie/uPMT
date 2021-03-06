package utils.autoSuggestion.strategies;

import models.SchemaFolder;
import utils.GlobalVariables;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyFolder extends SuggestionStrategy<SchemaFolder> {
    @Override
    public Map<String, SchemaFolder> fetchSuggestions() {
        // Create a list with all the possibilities: the list where we search the entered text for a match
        Map<String, SchemaFolder> result = new HashMap<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: GlobalVariables.getSchemaTreeRoot().foldersProperty()) {
            result.put(folder.getName(), folder);
            iterateOverFolder(folder, result);
        }
        return result;
    }

    protected void iterateOverFolder(SchemaFolder folder, Map<String, SchemaFolder> result) {
        // Used by fetchSuggestions
        // Iterate over the tree
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            // recursively call
            result.put(subFolder.getName(), folder);
            iterateOverFolder(subFolder, result);
        }
    }
}

package utils.autoSuggestion.strategies;

import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.SchemaTreePluggable;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyFolder extends SuggestionStrategy {
    @Override
    public Map<String, SchemaTreePluggable> fetchSuggestions() {
        // Create a list with all the possibilities: the list where we search the entered text for a match
        Map<String, SchemaTreePluggable> result = new HashMap<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: getSchemaTreeRoot().foldersProperty()) {
            result.put(folder.getName(), folder);
            iterateOverFolder(folder, result);
        }
        return result;
    }

    @Override
    protected void iterateOverFolder(SchemaFolder folder, Map<String, SchemaTreePluggable> result) {
        // Used by fetchSuggestions
        // Iterate over the tree

        // Loop over the folders
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            // recursively call
            result.put(folder.getName(), folder);
            iterateOverFolder(subFolder, result);
        }
    }
}

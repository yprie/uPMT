package utils.autoSuggestion.strategies;

import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.SchemaTreePluggable;
import utils.autoSuggestion.AutoSuggestions;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyCategory extends SuggestionStrategy {
    public Map<String, SchemaTreePluggable> fetchSuggestions() {
        // Create a list with all the possibilities: the list where we search the entered text for a match
        Map<String, SchemaTreePluggable> result = new HashMap<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: AutoSuggestions.getSchemaTreeRoot().foldersProperty()) {
            iterateOverFolder(folder, result);
        }
        return result;
    }

    private void iterateOverFolder(SchemaFolder folder, Map<String, SchemaTreePluggable> result) {
        // Used by fetchSuggestions
        // Iterate over the tree

        // Loop over the folders
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            // recursively call
            iterateOverFolder(subFolder, result);
        }

        // Loop over the categories
        for(SchemaCategory category: folder.categoriesProperty()) {
            result.put(category.getName(), category);
        }
    }
}

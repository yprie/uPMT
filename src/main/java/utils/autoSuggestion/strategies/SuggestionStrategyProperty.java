package utils.autoSuggestion.strategies;

import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import utils.autoSuggestion.AutoSuggestions;

import java.util.HashMap;
import java.util.Map;

public class SuggestionStrategyProperty  extends SuggestionStrategy<SchemaProperty> {
    @Override
    public Map<String, SchemaProperty> fetchSuggestions() {
        // Create a list with all the possibilities: the list where we search the entered text for a match
        Map<String, SchemaProperty> result = new HashMap<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: AutoSuggestions.getSchemaTreeRoot().foldersProperty()) {
            iterateOverFolder(folder, result);
        }
        return result;
    }

    private void iterateOverFolder(SchemaFolder folder, Map<String, SchemaProperty> result) {
        // Used by fetchSuggestions
        // Iterate over the tree

        // Loop over the folders
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            // recursively call
            iterateOverFolder(subFolder, result);
        }

        // Loop over the categories
        for(SchemaCategory category: folder.categoriesProperty()) {
            iterateOverCategory(category, result);
        }
    }

    private void iterateOverCategory(SchemaCategory category, Map<String, SchemaProperty> result) {
        // Used by fetchSuggestions
        // Iterate over the tree

        // Loop over the properties
        for(SchemaProperty property: category.propertiesProperty()) {
            result.put(property.getName(), property);
        }
    }
}

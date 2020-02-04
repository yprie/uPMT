package utils.autoSuggestion.strategies;

import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import components.schemaTree.Cell.SchemaTreePluggable;

import java.util.Map;

public abstract class SuggestionStrategy {
    private SchemaTreeRoot root;

    public void setSchemaTreeRoot(SchemaTreeRoot root) {
        this.root = root;
    }
    public SchemaTreeRoot getSchemaTreeRoot() {
        return root;
    }
    public abstract Map<String, SchemaTreePluggable> fetchSuggestions();

    protected void iterateOverFolder(SchemaFolder folder, Map<String, SchemaTreePluggable> result) {
        // Used by fetchSuggestions
        // Iterate over the tree

        // Loop over the folders
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            result.put(subFolder.getName(), subFolder);
            // recursively call
            iterateOverFolder(subFolder, result);
        }

        // Loop over the categories
        for(SchemaCategory category: folder.categoriesProperty()) {
            result.put(category.getName(), category);
        }
    }
}

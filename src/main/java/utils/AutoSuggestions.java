package utils;

import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaTreeRoot;
import components.schemaTree.Cell.SchemaTreePluggable;

import java.util.HashMap;
import java.util.Map;

public class AutoSuggestions {

    private static final AutoSuggestions autoSuggestions = new AutoSuggestions();

    private SchemaTreeRoot root;

    private AutoSuggestions() {}

    public static AutoSuggestions getAutoSuggestions() {
        return autoSuggestions;
    }

    public void setSchemaTreeRoot(SchemaTreeRoot root) {
        this.root = root;
    }

    public Map<String, SchemaTreePluggable> getSuggestions() {
        Map<String, SchemaTreePluggable> result = new HashMap<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: root.foldersProperty()) {
            result.put(folder.getName(), folder);
            iterateOverFolder(folder, result);
        }
        return result;
    }

    private void iterateOverFolder(SchemaFolder folder, Map<String, SchemaTreePluggable> result) {
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

package utils;

import components.schemaTree.Cell.Models.SchemaCategory;
import components.schemaTree.Cell.Models.SchemaFolder;
import components.schemaTree.Cell.Models.SchemaTreeRoot;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getSuggestions() {
        List<String> result = new ArrayList<>();

        // Loop over the folders of the root
        for(SchemaFolder folder: root.foldersProperty()) {
            result.add(folder.getName());
            iterateOverFolder(folder, result);
        }
        return result;
    }

    private void iterateOverFolder(SchemaFolder folder, List<String> result) {
        // Iterate over the tree

        // Loop over the folders
        for(SchemaFolder subFolder: folder.foldersProperty()) {
            result.add(subFolder.getName());
            // recursively call
            iterateOverFolder(subFolder, result);
        }

        // Loop over the categories
        for(SchemaCategory category: folder.categoriesProperty()) {
            result.add(category.getName());
        }
    }
}

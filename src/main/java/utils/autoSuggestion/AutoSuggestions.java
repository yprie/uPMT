package utils.autoSuggestion;

import components.schemaTree.Cell.Models.SchemaTreeRoot;

public class AutoSuggestions {

    private static final AutoSuggestions autoSuggestions = new AutoSuggestions();

    private static SchemaTreeRoot root;

    private AutoSuggestions() {} // private constructor

    public static AutoSuggestions getAutoSuggestions() {
        return autoSuggestions;
    }

    public void setSchemaTreeRoot(SchemaTreeRoot root) {
        this.root = root;
    }
    public static SchemaTreeRoot getSchemaTreeRoot() {
        return root;
    }
}

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
    protected abstract void iterateOverFolder(SchemaFolder folder, Map<String, SchemaTreePluggable> result);
}

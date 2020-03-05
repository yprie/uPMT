package utils.autoSuggestion;

import models.RootMoment;
import models.SchemaTreeRoot;

public class AutoSuggestions {
    /*
    A class used by the suggestion strategies.
    This is a singleton.
     */

    private static final AutoSuggestions autoSuggestions = new AutoSuggestions();

    private static SchemaTreeRoot root;
    private static RootMoment rootMoment;

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

    public void setRootMoment(RootMoment rootMment) {
        this.rootMoment = rootMment;
    }
    public static RootMoment getRootMoment() {
        return rootMoment;
    }
}

package utils;

import models.RootMoment;
import models.SchemaTreeRoot;

public class GlobalVariables {
    /*
    A class used by the suggestion strategies and for links between interview and modeling space
    This is a singleton.
     */

    private static final GlobalVariables globalVariables = new GlobalVariables();

    private static SchemaTreeRoot root;
    private static RootMoment rootMoment;

    private GlobalVariables() {} // private constructor

    public static GlobalVariables getGlobalVariables() {
        return globalVariables;
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

package components.schemaTree.Cell;

import utils.removable.IRemovable;
import components.schemaTree.Cell.Models.*;

public class Utils {

    public static boolean IsSchemaTreeRoot(SchemaTreePluggable item) {
        return item.getDataFormat() == SchemaTreeRoot.format;
    }

    public static boolean IsSchemaTreeFolder(SchemaTreePluggable item){
        return item.getDataFormat() == SchemaFolder.format;
    }

    public static boolean IsSchemaTreeCategory(SchemaTreePluggable item){
        return item.getDataFormat() == SchemaCategory.format;
    }

    public static boolean IsSchemaTreeProperty(SchemaTreePluggable item){
        return item.getDataFormat() == SchemaProperty.format;
    }

}

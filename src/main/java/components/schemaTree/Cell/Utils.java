package components.schemaTree.Cell;

import models.SchemaMomentType;
import javafx.scene.control.TreeItem;
import models.SchemaCategory;
import models.SchemaFolder;
import models.SchemaProperty;
import models.SchemaTreeRoot;

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

    public static boolean IsSchemaTreeMomentType(SchemaTreePluggable item){
        return item.getDataFormat() == SchemaMomentType.format;
    }

    public static boolean IsSameType(SchemaTreePluggable a, SchemaTreePluggable b) {
        return a.getDataFormat() == b.getDataFormat();
    }

    public static <E, T> TreeItem<T> findTreeElement(TreeItem<E> item , T value)
    {
        if(item == null)
            return null;

        if (item.getValue() == value)
            return (TreeItem<T>) item;

        for (TreeItem<E> child : item.getChildren()){
            TreeItem<T> r = findTreeElement(child, value);
            if(r != null)
                return r;
        }
        return null;
    }
}

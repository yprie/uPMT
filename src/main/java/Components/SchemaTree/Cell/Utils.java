package Components.SchemaTree.Cell;

import application.History.HistoryManager;
import utils.Removable.IRemovable;
import Components.SchemaTree.Cell.Commands.RemoveSchemaTreePluggable;
import Components.SchemaTree.Cell.Models.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Utils {

    public static final <E extends IRemovable & SchemaTreePluggable> void setupListenerOnChildRemoving(SchemaTreePluggable parent, E e) {
        e.existsProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(aBoolean != t1 && !t1){
                    HistoryManager.addCommand(new RemoveSchemaTreePluggable(parent, e), false);
                    observableValue.removeListener(this);
                }
            }
        });
    }

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

package Components.InterviewTree;

import Components.InterviewTree.Commands.DeleteItemPluggable;
import application.History.HistoryManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import utils.Removable.IRemovable;

public class Utils {
    public static final <E extends IRemovable & InterviewTreePluggable> void setupListenerOnDelete(InterviewTreePluggable parent, E e) {
        e.existsProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(aBoolean != t1 && !t1){
                    HistoryManager.addCommand(new DeleteItemPluggable(parent, e), false);
                    observableValue.removeListener(this);
                }
            }
        });
    }
}

package components.modelisationSpace.hooks;

import models.Moment;
import models.SchemaCategory;

public class ModelisationSpaceHookNotifier {

    private ModelisationSpaceHook hook;

    public ModelisationSpaceHookNotifier(ModelisationSpaceHook hook) {
        this.hook = hook;
    }

    public void notifyCategoryAdded(SchemaCategory schemaCategory) {
        this.hook.onCategoryAddedListeners.forEach(listener -> {
            listener.accept(schemaCategory);
        });
    }

    public void notifyCategoryRemoved(SchemaCategory schemaCategory) {
        this.hook.onCategoryRemovedListeners.forEach(listener -> {
            listener.accept(schemaCategory);
        });
    }

    public void notifyMomentAdded(Moment moment) {
        this.hook.onMomentAddedListeners.forEach(listener -> {
            listener.accept(moment);
        });
    }

    public void notifyMomentRemoved(Moment moment) {
        this.hook.onMomentRemovedListeners.forEach(listener -> {
            listener.accept(moment);
        });
    }

}

package components.modelisationSpace.hooks;

import javafx.scene.control.skin.SliderSkin;
import models.ConcreteCategory;
import models.ConcreteProperty;
import models.Moment;
import models.SchemaCategory;

public class ModelisationSpaceHookNotifier {

    private ModelisationSpaceHook hook;

    public ModelisationSpaceHookNotifier(ModelisationSpaceHook hook) {
        this.hook = hook;
    }

    public void notifyConcreteCategoryAdded(ConcreteCategory cc) {
        this.hook.onConcreteCategoryAddedListeners.forEach(listener -> {
            listener.accept(cc);
        });
    }

    public void notifyConcreteCategoryRemoved(ConcreteCategory cc) {
        this.hook.onConcreteCategoryRemovedListeners.forEach(listener -> {
            listener.accept(cc);
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

    public void notifyConcretePropertyValueChanged(String oldValue, ConcreteProperty concreteProperty) {
        this.hook.onConcretePropertyValueChanged.forEach(listener -> {
            listener.accept(oldValue, concreteProperty);
        });
    }

}

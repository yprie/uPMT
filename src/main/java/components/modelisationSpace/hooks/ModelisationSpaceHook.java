package components.modelisationSpace.hooks;

import models.ConcreteCategory;
import models.ConcreteProperty;
import models.Moment;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelisationSpaceHook {

    ArrayList<Consumer<ConcreteCategory>> onConcreteCategoryAddedListeners = new ArrayList<>();
    ArrayList<Consumer<ConcreteCategory>> onConcreteCategoryRemovedListeners = new ArrayList<>();

    ArrayList<Consumer<Moment>> onMomentAddedListeners = new ArrayList<>();
    ArrayList<Consumer<Moment>> onMomentRemovedListeners = new ArrayList<>();

    ArrayList<BiConsumer<String, ConcreteProperty>> onConcretePropertyValueChanged = new ArrayList<>();

    public void addOnConcreteCategoryAdded(Consumer<ConcreteCategory> consumer) {
        this.onConcreteCategoryAddedListeners.add(consumer);
    }
    public void addOnConcreteCategoryRemoved(Consumer<ConcreteCategory> consumer) { this.onConcreteCategoryRemovedListeners.add(consumer); }

    public void addOnMomentAdded(Consumer<Moment> consumer) {
        this.onMomentAddedListeners.add(consumer);
    }
    public void addOnMomentRemoved(Consumer<Moment> consumer) {
        this.onMomentRemovedListeners.add(consumer);
    }

    public void addOnConcretePropertyValueChanged(BiConsumer<String, ConcreteProperty> consumer) { this.onConcretePropertyValueChanged.add(consumer); }

    /*public void resetListeners() {
        onConcreteCategoryAddedListeners.clear();
        onConcreteCategoryRemovedListeners.clear();
        onMomentAddedListeners.clear();
        onMomentRemovedListeners.clear();
        onConcretePropertyValueChanged.clear();
    }*/
}

package components.modelisationSpace.hooks;

import models.Moment;
import models.SchemaCategory;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ModelisationSpaceHook {

    ArrayList<Consumer<SchemaCategory>> onCategoryAddedListeners = new ArrayList<>();
    ArrayList<Consumer<SchemaCategory>> onCategoryRemovedListeners = new ArrayList<>();

    ArrayList<Consumer<Moment>> onMomentAddedListeners = new ArrayList<>();
    ArrayList<Consumer<Moment>> onMomentRemovedListeners = new ArrayList<>();

    public ModelisationSpaceHook() { }

    public void addOnCategoryAdded(Consumer<SchemaCategory> consumer) {
        this.onCategoryAddedListeners.add(consumer);
    }
    public void addOnCategoryRemoved(Consumer<SchemaCategory> consumer) { this.onCategoryRemovedListeners.add(consumer); }

    public void addOnMomentAdded(Consumer<Moment> consumer) {
        this.onMomentAddedListeners.add(consumer);
    }
    public void addOnMomentRemoved(Consumer<Moment> consumer) {
        this.onMomentRemovedListeners.add(consumer);
    }

    public void resetListeners() {
        onCategoryAddedListeners.clear();
        onCategoryRemovedListeners.clear();
        onMomentAddedListeners.clear();
        onMomentRemovedListeners.clear();
    }
}

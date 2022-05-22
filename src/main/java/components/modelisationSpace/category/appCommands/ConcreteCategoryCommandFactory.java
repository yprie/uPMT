package components.modelisationSpace.category.appCommands;

import components.modelisationSpace.hooks.ModelisationSpaceHookNotifier;
import models.ConcreteCategory;
import models.Moment;
import models.SchemaCategory;

public class ConcreteCategoryCommandFactory {

    private ModelisationSpaceHookNotifier hookNotifier;
    private Moment parent;

    public ConcreteCategoryCommandFactory(ModelisationSpaceHookNotifier hookNotifier, Moment parent) {
        this.hookNotifier = hookNotifier;
        this.parent = parent;
    }

    public AddConcreteCategoryCommand addConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new AddConcreteCategoryCommand(hookNotifier, parent, c, userCommand); }
    public AddConcreteCategoryCommand addSchemaCategoryCommand(SchemaCategory sc, boolean userCommand) { return new AddConcreteCategoryCommand(hookNotifier, parent, sc, userCommand); }
    public RemoveConcreteCategoryCommand removeConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new RemoveConcreteCategoryCommand(hookNotifier, parent, c, c.getController(), userCommand); }

    public ModelisationSpaceHookNotifier getHookNotifier() { return hookNotifier; }
}

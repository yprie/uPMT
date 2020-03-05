package components.modelisationSpace.category.appCommands;

import models.ConcreteCategory;
import models.Moment;

public class ConcreteCategoryCommandFactory {

    private Moment parent;

    public ConcreteCategoryCommandFactory(Moment parent) {
        this.parent = parent;
    }

    public AddConcreteCategoryCommand addConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new AddConcreteCategoryCommand(parent, c, userCommand); }
    public RemoveConcreteCategoryCommand removeConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new RemoveConcreteCategoryCommand(parent, c, userCommand); }
}

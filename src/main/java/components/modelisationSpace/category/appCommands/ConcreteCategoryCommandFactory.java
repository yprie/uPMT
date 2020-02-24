package components.modelisationSpace.category.appCommands;

import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.moment.model.Moment;

public class ConcreteCategoryCommandFactory {

    private Moment parent;

    public ConcreteCategoryCommandFactory(Moment parent) {
        this.parent = parent;
    }

    public AddConcreteCategoryCommand addConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new AddConcreteCategoryCommand(parent, c, userCommand); }
    public RemoveConcreteCategoryCommand removeConcreteCategoryCommand(ConcreteCategory c, boolean userCommand) { return new RemoveConcreteCategoryCommand(parent, c, userCommand); }
}

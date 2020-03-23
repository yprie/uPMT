package components.modelisationSpace.category.modelCommands;

import application.history.ModelUserActionCommand;
import models.ConcreteCategory;
import models.Moment;

public class AddConcreteCategory extends ModelUserActionCommand<Void, Void> {

    private Moment moment;
    private ConcreteCategory concreteCategory;

    public AddConcreteCategory(Moment m, ConcreteCategory c) {
        this.moment = m;
        this.concreteCategory = c;
    }

    @Override
    public Void execute() {
        moment.addCategory(concreteCategory);
        return null;
    }

    @Override
    public Void undo() {
        moment.removeCategory(concreteCategory);
        return null;
    }
}

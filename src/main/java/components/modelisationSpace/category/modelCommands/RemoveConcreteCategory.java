package components.modelisationSpace.category.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.category.model.ConcreteCategory;
import components.modelisationSpace.moment.model.Moment;

public class RemoveConcreteCategory extends ModelUserActionCommand<Void, Void> {

    private Moment moment;
    private ConcreteCategory concreteCategory;
    int prevIndex;

    public RemoveConcreteCategory(Moment m, ConcreteCategory c) {
        this.moment = m;
        this.concreteCategory = c;
        this.prevIndex = moment.indexOfConcreteCategory(concreteCategory);
    }

    @Override
    public Void execute() {
        moment.removeCategory(concreteCategory);
        return null;
    }

    @Override
    public Void undo() {
        System.out.println("adding back !");
        moment.addCategory(prevIndex, concreteCategory);
        return null;
    }
}

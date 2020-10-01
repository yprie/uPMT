package components.modelisationSpace.category.modelCommands;

import application.history.ModelUserActionCommand;
import models.ConcreteCategory;
import models.Moment;

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

        // Remove the underlining of the descripteme deleted
        concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().removeDescripteme(descripteme);
        });
        concreteCategory.propertiesProperty().forEach(concreteProperty -> {
            concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().removeDescripteme(descripteme);
            });
        });
        return null;
    }

    @Override
    public Void undo() {
        moment.addCategory(prevIndex, concreteCategory);

        // Add the underlining of the descripteme deleted
        concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().addDescripteme(descripteme);
        });
        concreteCategory.propertiesProperty().forEach(concreteProperty -> {
            concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().addDescripteme(descripteme);
            });
        });
        return null;
    }
}

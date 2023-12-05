package components.modelisationSpace.category.modelCommands;

import application.history.ModelUserActionCommand;
import components.modelisationSpace.category.controllers.ConcreteCategoryController;
import models.ConcreteCategory;
import models.Moment;

public class RemoveConcreteCategory extends ModelUserActionCommand<Void, Void> {

    private Moment moment;
    private ConcreteCategory concreteCategory;
    private ConcreteCategoryController controller;

    public RemoveConcreteCategory(Moment m, ConcreteCategory c, ConcreteCategoryController controller) {
        this.moment = m;
        this.concreteCategory = c;
        this.controller = controller;
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
        concreteCategory.getSchemaCategory().removeFromControllers(controller);
        return null;
    }

    @Override
    public Void undo() {
        moment.addCategory(concreteCategory);

        // Add the underlining of the descripteme deleted
        concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().addDescripteme(descripteme);
        });
        concreteCategory.propertiesProperty().forEach(concreteProperty -> {
            concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().addDescripteme(descripteme);
            });
        });
        concreteCategory.getSchemaCategory().addToControllers(controller);
        return null;
    }
}

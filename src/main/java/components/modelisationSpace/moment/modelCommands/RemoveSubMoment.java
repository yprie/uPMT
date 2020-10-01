package components.modelisationSpace.moment.modelCommands;

import application.history.ModelUserActionCommand;
import models.Moment;
import models.RootMoment;

public class RemoveSubMoment extends ModelUserActionCommand {

    private RootMoment parent;
    private Moment moment;
    private int oldIndex ;


    public RemoveSubMoment(RootMoment parent, Moment moment) {
        this.parent = parent;
        this.moment = moment;
    }

    @Override
    public Void execute() {
        oldIndex = parent.indexOf(moment);
        parent.removeMoment(moment);

        // Remove the underlining of the descripteme deleted
        moment.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().removeDescripteme(descripteme);
        });
        moment.concreteCategoriesProperty().forEach(concreteCategory -> {
            concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().removeDescripteme(descripteme);
            });
            concreteCategory.propertiesProperty().forEach(concreteProperty -> {
                concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                    descripteme.getInterviewText().removeDescripteme(descripteme);
                });
            });
        });
        moment.momentsProperty().forEach(subMoment -> {
            subMoment.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().removeDescripteme(descripteme);
            });
            subMoment.concreteCategoriesProperty().forEach(concreteCategory -> {
                concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
                    descripteme.getInterviewText().removeDescripteme(descripteme);
                });
                concreteCategory.propertiesProperty().forEach(concreteProperty -> {
                    concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                        descripteme.getInterviewText().removeDescripteme(descripteme);
                    });
                });
            });
        });
        return null;
    }

    @Override
    public Void undo() {
        parent.addMoment(oldIndex, moment);

        // Add the underlining of the descripteme deleted
        moment.getJustification().descriptemesProperty().forEach(descripteme -> {
            descripteme.getInterviewText().addDescripteme(descripteme);
        });
        moment.concreteCategoriesProperty().forEach(concreteCategory -> {
            concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().addDescripteme(descripteme);
            });
            concreteCategory.propertiesProperty().forEach(concreteProperty -> {
                concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                    descripteme.getInterviewText().addDescripteme(descripteme);
                });
            });
        });
        moment.momentsProperty().forEach(subMoment -> {
            subMoment.getJustification().descriptemesProperty().forEach(descripteme -> {
                descripteme.getInterviewText().addDescripteme(descripteme);
            });
            subMoment.concreteCategoriesProperty().forEach(concreteCategory -> {
                concreteCategory.getJustification().descriptemesProperty().forEach(descripteme -> {
                    descripteme.getInterviewText().addDescripteme(descripteme);
                });
                concreteCategory.propertiesProperty().forEach(concreteProperty -> {
                    concreteProperty.getJustification().descriptemesProperty().forEach(descripteme -> {
                        descripteme.getInterviewText().addDescripteme(descripteme);
                    });
                });
            });
        });
        return null;
    }
}

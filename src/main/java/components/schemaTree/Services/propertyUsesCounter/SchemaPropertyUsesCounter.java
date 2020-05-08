package components.schemaTree.Services.propertyUsesCounter;

import components.modelisationSpace.hooks.ModelisationSpaceHook;
import components.schemaTree.Services.categoryUsesCounter.ResetCategoryUsesCounterVisitor;

import models.*;

public class SchemaPropertyUsesCounter {

    private ModelisationSpaceHook modelisationSpaceHook;
    private enum CountingMethod { INCREMENT, DECREMENT };

    public SchemaPropertyUsesCounter(Project project, ModelisationSpaceHook modelisationSpaceHook) {
        initalize(project);
        setupModelisationSpaceHooks(modelisationSpaceHook);
    }

    public void setupModelisationSpaceHooks(ModelisationSpaceHook msh) {
        modelisationSpaceHook = msh;

        modelisationSpaceHook.addOnConcreteCategoryAdded(concreteCategory -> {
            countThroughACategory(concreteCategory, CountingMethod.INCREMENT);
        });

        modelisationSpaceHook.addOnConcreteCategoryAdded(concreteCategory -> {
            countThroughACategory(concreteCategory, CountingMethod.DECREMENT);
        });

        modelisationSpaceHook.addOnMomentAdded(moment -> {
            countThroughAMoment(moment, CountingMethod.INCREMENT);
        });

        modelisationSpaceHook.addOnMomentRemoved(moment -> {
            countThroughAMoment(moment, CountingMethod.DECREMENT);
        });

        modelisationSpaceHook.addOnConcretePropertyValueChanged((oldValue, concreteProperty) -> {
            SchemaProperty sp = concreteProperty.getSchemaProperty();
            int i = 0;
            if(!oldValue.equals(concreteProperty.getValue())) {
                if(oldValue.isEmpty())
                    i = 1;
                else if(concreteProperty.getValue().isEmpty())
                    i = -1;
            }
            if(i != 0)
                sp.setNumberOfUsesInModelisation(sp.numberOfUsesInModelisationProperty().get() + i);
        });
    }

    private void reinitializeCounters(Project project) {
        project.getSchemaTreeRoot().accept(new ResetPropertyUsesCounterVisitor());
    }

    private void initalize(Project project) {
        reinitializeCounters(project);
        project.interviewsProperty().forEach(interview -> {
            interview.getRootMoment().momentsProperty().forEach(moment -> { countThroughAMoment(moment, CountingMethod.INCREMENT );});
        });
    }

    private void countThroughAMoment(Moment m, CountingMethod cm) {
        m.concreteCategoriesProperty().forEach(concreteCategory -> {
            countThroughACategory(concreteCategory, cm);
        });
        m.momentsProperty().forEach(moment -> { countThroughAMoment(moment, cm); });
    }

    private void countThroughACategory(ConcreteCategory c, CountingMethod cm) {
        c.propertiesProperty().forEach(concreteProperty -> {
            countThroughAProperty(concreteProperty, cm);
        });
    }

    private void countThroughAProperty(ConcreteProperty p, CountingMethod cm) {
        SchemaProperty sp = p.getSchemaProperty();
        int i = 0;
        if(cm == CountingMethod.INCREMENT) {
            i = !p.getValue().isEmpty() ? 1 : 0;
        }
        else if(cm == CountingMethod.DECREMENT) {
            i = !p.getValue().isEmpty() ? -1 : 0;
        }
        sp.setNumberOfUsesInModelisation(sp.numberOfUsesInModelisationProperty().get() + i);
    }
}

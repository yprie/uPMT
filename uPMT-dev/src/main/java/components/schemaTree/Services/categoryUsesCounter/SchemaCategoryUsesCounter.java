package components.schemaTree.Services.categoryUsesCounter;

import components.modelisationSpace.hooks.ModelisationSpaceHook;
import models.Moment;
import models.Project;
import models.SchemaCategory;

public class SchemaCategoryUsesCounter {

    private ModelisationSpaceHook modelisationSpaceHook;
    private enum CountingMethod { INCREMENT, DECREMENT };

    public SchemaCategoryUsesCounter(Project project, ModelisationSpaceHook modelisationSpaceHook) {
        initalize(project);
        setupModelisationSpaceHooks(modelisationSpaceHook);
    }

    public void setupModelisationSpaceHooks(ModelisationSpaceHook msh) {
        modelisationSpaceHook = msh;
        modelisationSpaceHook.addOnConcreteCategoryAdded(cc -> {
            SchemaCategory sc = cc.getSchemaCategory();
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() + 1);
        });

        modelisationSpaceHook.addOnConcreteCategoryRemoved(cc -> {
            SchemaCategory sc = cc.getSchemaCategory();
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() - 1);
        });

        modelisationSpaceHook.addOnMomentAdded(moment -> {
            countThroughAMoment(moment, CountingMethod.INCREMENT);
        });

        modelisationSpaceHook.addOnMomentRemoved(moment -> {
            countThroughAMoment(moment, CountingMethod.DECREMENT);
        });
    }

    private void reinitializeCounters(Project project) {
        project.getSchemaTreeRoot().accept(new ResetCategoryUsesCounterVisitor());
    }

    private void initalize(Project project) {
        reinitializeCounters(project);
        project.interviewsProperty().forEach(interview -> {
            interview.getRootMoment().momentsProperty().forEach(moment -> { countThroughAMoment(moment, CountingMethod.INCREMENT );});
        });
    }

    private void countThroughAMoment(Moment m, CountingMethod cm) {
        m.concreteCategoriesProperty().forEach(concreteCategory -> {
            SchemaCategory sc = concreteCategory.getSchemaCategory();
            int i = cm == CountingMethod.INCREMENT ? 1 : cm == CountingMethod.DECREMENT ? -1 : 0;
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() + i);
        });
        m.momentsProperty().forEach(moment -> { countThroughAMoment(moment, cm); });
    }
}

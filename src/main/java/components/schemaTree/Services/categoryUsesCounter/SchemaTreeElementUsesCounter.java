package components.schemaTree.Services.categoryUsesCounter;

import components.modelisationSpace.hooks.ModelisationSpaceHook;
import models.Moment;
import models.Project;
import models.SchemaCategory;

public class SchemaTreeElementUsesCounter {

    private enum CountingMethod { INCREMENT, DECREMENT };

    public SchemaTreeElementUsesCounter(Project project, ModelisationSpaceHook modelisationSpaceHook) {
        initalize(project);

        modelisationSpaceHook.addOnCategoryAdded(schemaCategory -> {
            schemaCategory.setNumberOfUsesInModelisation(schemaCategory.getNumberOfUsesInModelisation().get() + 1);
        });

        modelisationSpaceHook.addOnCategoryRemoved(schemaCategory -> {
            schemaCategory.setNumberOfUsesInModelisation(schemaCategory.getNumberOfUsesInModelisation().get() - 1);
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
            sc.setNumberOfUsesInModelisation(sc.getNumberOfUsesInModelisation().get() + i);
        });
        m.momentsProperty().forEach(moment -> { countThroughAMoment(moment, cm); });
    }
}

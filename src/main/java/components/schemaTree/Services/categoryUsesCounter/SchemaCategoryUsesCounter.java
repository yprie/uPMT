package components.schemaTree.Services.categoryUsesCounter;

import components.modelisationSpace.hooks.ModelisationSpaceHook;
import models.Interview;
import models.Moment;
import models.Project;
import models.SchemaCategory;

public class SchemaCategoryUsesCounter {
    private Project project;//to be able to spot the interview where the categories is added
    private ModelisationSpaceHook modelisationSpaceHook;
    private enum CountingMethod { INCREMENT, DECREMENT };

    public SchemaCategoryUsesCounter(Project project, ModelisationSpaceHook modelisationSpaceHook) {
        this.project=project;
        initalize(project);
        setupModelisationSpaceHooks(modelisationSpaceHook);
    }

    public void setupModelisationSpaceHooks(ModelisationSpaceHook msh) {
        modelisationSpaceHook = msh;
        modelisationSpaceHook.addOnConcreteCategoryAdded(cc -> {
            SchemaCategory sc = cc.getSchemaCategory();
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() + 1);
            sc.setNumberOfUsesInInterview(this.project.getSelectedInterview(),sc.numberOfUsesInInterviewProperty(this.project.getSelectedInterview()) + 1);
        });

        modelisationSpaceHook.addOnConcreteCategoryRemoved(cc -> {
            SchemaCategory sc = cc.getSchemaCategory();
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() - 1);
            sc.setNumberOfUsesInInterview(this.project.getSelectedInterview(),sc.numberOfUsesInInterviewProperty(this.project.getSelectedInterview()) - 1);

        });

        modelisationSpaceHook.addOnMomentAdded(moment -> {
            //countThroughAMoment(moment, CountingMethod.INCREMENT);
            countThroughAMomentInterview(this.project.getSelectedInterview(),moment, CountingMethod.INCREMENT);
        });

        modelisationSpaceHook.addOnMomentRemoved(moment -> {
            //countThroughAMoment(moment, CountingMethod.DECREMENT);
            countThroughAMomentInterview(this.project.getSelectedInterview(),moment, CountingMethod.DECREMENT);
        });
    }

    private void reinitializeCounters(Project project) {
        project.getSchemaTreeRoot().accept(new ResetCategoryUsesCounterVisitor());
    }

    private void initalize(Project project) {
        reinitializeCounters(project);
        project.interviewsProperty().forEach(interview -> {
            interview.getRootMoment().momentsProperty().forEach(moment -> { countThroughAMomentInterview(interview,moment, CountingMethod.INCREMENT );});
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
    //newer version of the function above to take into consideration uses per interview
    private void countThroughAMomentInterview(Interview interview, Moment m, CountingMethod cm) {
        m.concreteCategoriesProperty().forEach(concreteCategory -> {
            SchemaCategory sc = concreteCategory.getSchemaCategory();
            int i = cm == CountingMethod.INCREMENT ? 1 : cm == CountingMethod.DECREMENT ? -1 : 0;
            sc.setNumberOfUsesInModelisation(sc.numberOfUsesInModelisationProperty().get() + i);
            sc.setNumberOfUsesInInterview(interview,sc.numberOfUsesInInterviewProperty(interview) + i);

        });
        m.momentsProperty().forEach(moment -> { countThroughAMomentInterview(interview,moment, cm); });
    }
}

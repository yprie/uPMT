package utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import models.*;

import java.util.HashSet;
import java.util.function.BiFunction;

public class GlobalVariables {
    /*
    A class used by the suggestion strategies and for links between interview and modeling space
    This is a singleton.
     */

    private static final GlobalVariables globalVariables = new GlobalVariables();

    private static SchemaTreeRoot root;
    private static RootMoment rootMoment;
    private static Project project;
    private static String currentProjectPath;
    private static SimpleObjectProperty<Descripteme> changedDescripteme = new SimpleObjectProperty<>();


    private GlobalVariables() {}

    private void iterateOverSubMoment(Moment moment, HashSet result, BiFunction<Moment, HashSet, Void> computeMoment) {
        for (Moment subMoment: moment.momentsProperty()) {
            computeMoment.apply(subMoment, result);
            iterateOverSubMoment(subMoment, result, computeMoment);
        }
    }

    public static GlobalVariables getGlobalVariables() {
        return globalVariables;
    }

    public void setSchemaTreeRoot(SchemaTreeRoot root) {
        this.root = root;
    }
    public static SchemaTreeRoot getSchemaTreeRoot() {
        return root;
    }

    public void setRootMoment(RootMoment rootMment) {
        this.rootMoment = rootMment;
    }
    public static RootMoment getRootMoment() {
        return rootMoment;
    }

    public void setDescriptemeChanged(Descripteme descripteme) {
        changedDescripteme.set(descripteme);
    }
    public ObservableObjectValue<Descripteme> getDescriptemeChangedProperty() {
        return changedDescripteme;
    }

    public Project getProject() {
        return project;
    }

    public  void setProject(Project project) {
        globalVariables.project = project;
    }

    public String getCurrentProjectPath() {
        return currentProjectPath;
    }

    public static void setCurrentProjectPath(String currentProjectPath) {
        globalVariables.currentProjectPath = currentProjectPath;
    }
    public HashSet<Descripteme> getAllDescripteme() {
        BiFunction<Moment, HashSet, Void> computeMoment = (Moment moment, HashSet result) -> {
            // Add the descriptems of the moment
            result.addAll(moment.getJustification().descriptemesProperty());

            for (ConcreteCategory concreteCategory : moment.concreteCategoriesProperty()) {
                result.addAll(concreteCategory.getJustification().descriptemesProperty());
                for (ConcreteProperty concreteProperty : concreteCategory.propertiesProperty()) {
                    result.addAll(concreteProperty.getJustification().descriptemesProperty());
                }
            }
            return null;
        };

        HashSet<Descripteme> result = new HashSet();
        for (Moment subMoment : rootMoment.momentsProperty()) {
            computeMoment.apply(subMoment, result);
            iterateOverSubMoment(subMoment, result, computeMoment);
        }
        return result;
    }

    public HashSet<Moment> getMomentsByDescripteme(Descripteme descripteme) {
        BiFunction<Moment, HashSet, Void> computeMoment = (Moment moment, HashSet result) -> {
            if(moment.getJustification().descriptemesProperty().contains(descripteme)) {
                result.add(moment);
            }
            for (ConcreteCategory concreteCategory : moment.concreteCategoriesProperty()) {
                if(concreteCategory.getJustification().descriptemesProperty().contains(descripteme)) {
                    result.add(moment);
                }
                for (ConcreteProperty concreteProperty : concreteCategory.propertiesProperty()) {
                    if(concreteProperty.getJustification().descriptemesProperty().contains(descripteme)) {
                        result.add(moment);
                    }
                }
            }
            return null;
        };
        HashSet<Moment> result = new HashSet();
        for (Moment subMoment : rootMoment.momentsProperty()) {
            computeMoment.apply(subMoment, result);
            iterateOverSubMoment(subMoment, result, computeMoment);
        }
        return result;
    }

}
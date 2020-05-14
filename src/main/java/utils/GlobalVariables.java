package utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import models.*;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class GlobalVariables {
    /*
    A class used by the suggestion strategies and for links between interview and modeling space
    This is a singleton.
     */

    private static final GlobalVariables globalVariables = new GlobalVariables();

    private static SchemaTreeRoot root;
    private static RootMoment rootMoment;

    private static SimpleObjectProperty<Descripteme> changedDescripteme = new SimpleObjectProperty<>();


    private GlobalVariables() {} // private constructor

    private void iterateOverSubMoment(Moment moment, ArrayList result, BiFunction<Moment, ArrayList, Void> computeMoment) {
        for(Moment subMoment: moment.momentsProperty()) {
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

    public ArrayList<Descripteme> getAllDescripteme() {
        BiFunction<Moment, ArrayList, Void> computeMoment = (Moment moment, ArrayList result) -> {
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

        ArrayList<Descripteme> result = new ArrayList<Descripteme>();
        for (Moment subMoment : rootMoment.momentsProperty()) {
            computeMoment.apply(subMoment, result);
            iterateOverSubMoment(subMoment, result, computeMoment);
        }
        return result;
    }

    public ArrayList<Moment> getMomentsByDescripteme(Descripteme descripteme) {
        BiFunction<Moment, ArrayList, Void> computeMoment = (Moment moment, ArrayList result) -> {
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
        ArrayList<Moment> result = new ArrayList();
        for (Moment subMoment : rootMoment.momentsProperty()) {
            computeMoment.apply(subMoment, result);
            iterateOverSubMoment(subMoment, result, computeMoment);
        }
        return result;
    }
}
package utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import models.*;

import java.util.ArrayList;
import java.util.ListIterator;

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
        System.out.println("49 changed descripteme : " + descripteme);
        changedDescripteme.set(descripteme);
    }
    public ObservableObjectValue<Descripteme> getDescriptemeChangedProperty() {
        return changedDescripteme;
    }

    public ArrayList<Descripteme> getAllDescriteme() {
        ArrayList<Descripteme> result = new ArrayList<Descripteme>();
        for (Moment subMoment : rootMoment.momentsProperty()) {
            computeMoment(subMoment, result);
            iterateOverSubMoment(subMoment, result);
        }
        return result;
    }

    private void iterateOverSubMoment(Moment moment, ArrayList<Descripteme> result) {
        for(Moment subMoment: moment.momentsProperty()) {
            computeMoment(subMoment, result);
            iterateOverSubMoment(subMoment, result);
        }
    }

    private void computeMoment(Moment moment, ArrayList<Descripteme> result) {
        // Add the descriptems of the moment
        for (ListIterator<Descripteme> it = moment.getJustification().descriptemesProperty().listIterator(); it.hasNext(); ) {
            Descripteme descripteme = it.next();
            result.add(descripteme);
        }

        for (ListIterator<ConcreteCategory> itCategory = moment.concreteCategoriesProperty().listIterator(); itCategory.hasNext(); ) {
            ConcreteCategory concreteCategory = itCategory.next();
            for (ListIterator<Descripteme> itDescripteme = concreteCategory.getJustification().descriptemesProperty().listIterator(); itDescripteme.hasNext(); ) {
                Descripteme descripteme = itDescripteme.next();
                result.add(descripteme);
            }
            for (ListIterator<ConcreteProperty> itProperty = concreteCategory.propertiesProperty().listIterator(); itProperty.hasNext(); ) {
                ConcreteProperty concreteProperty = itProperty.next();
                for (ListIterator<Descripteme> itDescripteme = concreteProperty.getJustification().descriptemesProperty().listIterator(); itDescripteme.hasNext(); ) {
                    Descripteme descripteme = itDescripteme.next();
                    result.add(descripteme);
                }
            }
        }
    }
}

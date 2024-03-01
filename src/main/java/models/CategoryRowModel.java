package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryRowModel {

    private String name;
    private SchemaCategory category;
    private List<Interview> interviews;
    private List<Moment> moments;
    private ObservableList<SchemaProperty> properties;
    private List<ObservableStringValue> propertiesValues;
    private ReadOnlyIntegerProperty nbOfUse;
    private BooleanProperty selectedProperty;

    public CategoryRowModel(SchemaCategory category){
        this.category = category;
        this.name = category.getName();
        this.interviews = category.getInterviews();
        this.nbOfUse = category.numberOfUsesInModelisationProperty();
        this.interviews = FXCollections.observableArrayList(category.getInterviews());
        this.properties = FXCollections.observableArrayList(category.propertiesProperty());
        this.propertiesValues = FXCollections.observableArrayList();
        ObservableList<ConcreteProperty> propertiesConcrete = FXCollections.observableArrayList();
        for (SchemaProperty p : properties) {
            ConcreteProperty concreteProperty = new ConcreteProperty(p);
            propertiesConcrete.add(concreteProperty);
            this.propertiesValues.add(concreteProperty.valueProperty());
        }
        this.moments = FXCollections.observableArrayList();
        Set<Moment> uniqueMoments = new HashSet<>();
        for (Interview interview : interviews) {
            // On récupère les moments de chaque interview dans lesquels la catégorie est utilisée
            // l'objectif est d'avoir la listes des moments dans lesquels la catégorie est utilisée
            // ne pas ajouter les doublons
            // si le moment ne contient pas la catégorie, on l'ajoute pas
           for (Moment moment : interview.getRootMoment().momentsProperty()) {
                if (moment.containsCategory(category)) {
                    uniqueMoments.add(moment);
                }
            }

            //RootMoment rootMoment = interview.getRootMoment();
            //uniqueMoments.addAll(rootMoment.momentsProperty());
        }
        this.moments.addAll(uniqueMoments);

        this.selectedProperty = new SimpleBooleanProperty(false); // Initialisation à false
    }

    public SchemaCategory getCategory() {
        return this.category;
    }

    public ReadOnlyIntegerProperty getNbOfUses() {
        return this.nbOfUse;
    }

    public Boolean isSelected() {
        return selectedProperty.get();
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(List<Interview> interviews) {
        this.interviews = interviews;
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public void setMoments(List<Moment> moments) {
        this.moments = moments;
    }

    public ObservableList<SchemaProperty> getProperties() {
        return properties;
    }

    public void setProperties(ObservableList<SchemaProperty> properties) {
        this.properties = properties;
    }

    public void setPropertiesValues(List<ObservableStringValue> propertiesValues) {
        this.propertiesValues = propertiesValues;
    }

    public int getNbOfUse() {
        return nbOfUse.get();
    }

    public ReadOnlyIntegerProperty nbOfUseProperty() {
        return nbOfUse;
    }

    public List<ObservableStringValue> getPropertiesValues() {
        return this.propertiesValues;
    }

    public ReadOnlyBooleanProperty selectedProperty() {
        return selectedProperty;
    }
}

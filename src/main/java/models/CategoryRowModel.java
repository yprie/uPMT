package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class CategoryRowModel {

    private String name;
    private SchemaCategory category;
    private List<Interview> interviews;
    private List<Moment> moments;
    private ObservableList<SchemaProperty> properties;
    private List<ObservableStringValue> propertiesValues;
    private ReadOnlyIntegerProperty nbOfUse;
    private BooleanProperty selectedProperty;
    private Map<Interview, List<Moment>> interviewMomentsMap;


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
        this.interviewMomentsMap = new HashMap<>();
        List<Moment> moments = new ArrayList<>();

        for (Interview interview : interviews) {

            // On récupère les moments de chaque interview dans lesquels la catégorie est utilisée
            // l'objectif est d'avoir la listes des moments dans lesquels la catégorie est utilisée
            // ne pas ajouter les doublons
            // si le moment ne contient pas la catégorie, on l'ajoute pas
            moments = new ArrayList<Moment>();
            Set<Moment> visitedMoments = new HashSet<>(); // Pour éviter les doublons

            // Parcours des moments de premier niveau
            for (Moment moment : interview.getRootMoment().momentsProperty()) {
                processMoment(moment, moments, visitedMoments);
            }

            this.interviewMomentsMap.put(interview, moments);

        }

        this.moments.addAll(uniqueMoments);
        this.selectedProperty = new SimpleBooleanProperty(false);
    }

    public CategoryRowModel duplicate() {
        // This method should create and return a copy of this object
        CategoryRowModel duplicate = new CategoryRowModel(this.category);
        duplicate.setSelected(this.getSelected());
        duplicate.setInterviews(new ArrayList<>(this.interviews));
        duplicate.setMoments(new ArrayList<>(this.moments));
        duplicate.setProperties(FXCollections.observableArrayList(this.properties));
        duplicate.setPropertiesValues(new ArrayList<>(this.propertiesValues));
        // Note: The deep or shallow copy depends on the specific needs
        return duplicate;
    }

    public Map<Interview, List<Moment>> getInterviewMomentsMap() {
        return this.interviewMomentsMap;
    }

    public void movePropertyValue(ObservableStringValue propertyValue1,ObservableStringValue propertyValue2) {
        int index1 = propertiesValues.indexOf(propertyValue1);
        int index2 = propertiesValues.indexOf(propertyValue2);

        // Check if the first property value is in the list
        if (index1 >= 0) {
            // Move property value1 to the position of property value2
            if (index2 >= 0) {
                propertiesValues.set(index2, propertyValue1);
            } else {
                propertiesValues.add(propertyValue1);
            }
            // Remove the original property value1 or replace it with a null or an empty string
            propertiesValues.set(index1, null );
        }
    }

    public void addProperty(SchemaProperty property) {
        this.properties.add(property);
    }

    public void removeProperty(SchemaProperty property) {
        this.properties.remove(property);
        // Also need to remove values associated with this property
        ConcreteProperty concreteProperty = new ConcreteProperty(property);
        ObservableStringValue propertiesValue = concreteProperty.valueProperty();
        this.propertiesValues.remove(propertiesValue);
    }

    private void
    processMoment(Moment moment, List<Moment> momentNames, Set<Moment> visitedMoments) {
        if (moment.containsCategory(category)) {
            momentNames.add(moment);
            visitedMoments.add(moment);
        }

        // Parcours des sous-moments
        for (Moment subMoment : moment.momentsProperty()) {
            if (!visitedMoments.contains(subMoment)) {
                processMoment(subMoment, momentNames, visitedMoments);
            }
        }
    }


    public void addPropertyValue(ObservableStringValue propertyValue) {
        this.propertiesValues.add(propertyValue);
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

    public boolean getSelected() {return selectedProperty.get();}

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
        // Trier la liste par ordre alphabétique
        Collections.sort(interviews, new Comparator<Interview>() {
            @Override
            public int compare(Interview interview1, Interview interview2) {
                return interview1.getTitle().compareTo(interview2.getTitle());
            }
        });
        return interviews;
    }
    public void setInterviews(List<Interview> interviews) {
        this.interviews = interviews;
    }

    public List<Moment> getMoments() {
        // Trier la liste par ordre alphabétique
        Collections.sort(moments, new Comparator<Moment>() {
            @Override
            public int compare(Moment moment1, Moment moment2) {
                return moment1.getName().compareTo(moment2.getName());
            }
        });
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

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    public ConcreteCategory getConcreteCategory(Moment moment) {
        for (ConcreteCategory concreteCategory : moment.getCategories()) {
            if (concreteCategory.isSchemaCategory(this.category)) {
                return concreteCategory;
            }
        }
        return null;
    }
}